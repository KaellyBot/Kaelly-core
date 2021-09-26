package com.github.kaellybot.core.command.util;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.model.error.ErrorFactory;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.model.constant.PermissionScope;
import com.github.kaellybot.core.model.constant.Priority;
import com.github.kaellybot.core.util.annotation.Hidden;
import com.github.kaellybot.core.util.annotation.PriorityProcessing;
import com.github.kaellybot.core.util.annotation.SuperAdministrator;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.util.PermissionSet;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public abstract class AbstractCommand implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

    protected final String name;
    protected List<CommandArgument<Message>> arguments;
    private final boolean isAdmin;
    private final boolean isHidden;
    protected final DiscordTranslator translator;

    protected AbstractCommand(String name, List<CommandArgument<Message>> arguments, DiscordTranslator translator){
        super();
        this.name = name;
        this.isAdmin = this.getClass().isAnnotationPresent(SuperAdministrator.class);
        this.isHidden = this.getClass().isAnnotationPresent(Hidden.class);
        this.arguments = arguments;
        this.translator = translator;
    }

    @PostConstruct
    public void init() {
        this.arguments = Stream.concat(arguments.stream(),
                Stream.of(new CommonHelpArgument(this, translator)))
                .collect(Collectors.toList());
    }

    @Override
    public final Flux<Message> request(Interaction interaction, Language language) {
        return getBotPermissions(interaction)
                .zipWith(getUserPermissions(interaction))
                .flatMapMany(tuple -> Flux.fromIterable(arguments)
                        .filter(argument -> argument.triggerInteraction(interaction))
                        .sort(Comparator.comparing(CommandArgument::getPriority)).take(1)
                        .flatMap(argument -> argument.tryExecute(interaction, language, tuple.getT1(), tuple.getT2()))
                        .switchIfEmpty(manageMisusedCommandError(interaction, language, tuple.getT1())));
    }

    private Flux<Message> manageMisusedCommandError(Interaction interaction, Language language, PermissionSet permissions){
        return Flux.just(interaction.getMessage().map(message -> message.getContent().startsWith(getName())).orElse(false))
                .filter(Boolean.TRUE::equals)
                .flatMap(result -> sendException(interaction, language, permissions,
                        ErrorFactory.createMisusedCommandError(this)));
    }

    public Flux<Message> sendException(Interaction interaction, Language language, PermissionSet permissions, Error error){
        return interaction.getChannel()
                .filter(channel -> permissions.containsAll(PermissionScope.TEXT_PERMISSIONS.getPermissions()))
                .flatMapMany(channel -> channel.createMessage(translator.getLabel(language, error)))
                .switchIfEmpty(interaction.getUser().getPrivateChannel()
                        .flatMapMany(channel -> channel.createMessage(translator.getLabel(Constants.DEFAULT_LANGUAGE, error))))
                .onErrorResume(ClientException.isStatusCode(403), err -> Mono.empty());
    }

    private Mono<PermissionSet> getBotPermissions(Interaction interaction){
        return interaction.getChannel()
                .filter(TextChannel.class::isInstance)
                .map(TextChannel.class::cast)
                .flatMap(channel -> channel.getEffectivePermissions(interaction.getClient().getSelfId()));
    }

    private Mono<PermissionSet> getUserPermissions(Interaction interaction){
        return interaction.getMember().map(Member::getBasePermissions).orElse(Mono.empty());
    }

    @Override
    public String help(Language lg){
        return "**" + name + "** " + translator.getLabel(lg, name.toLowerCase() + ".help");
    }

    @Override
    public String moreHelp(Language lg){
        return help(lg) + arguments.stream()
                .filter(CommandArgument::isDescribed)
                .sorted(Comparator.comparing(CommandArgument::getOrder))
                .map(arg -> "\n" + arg.help(lg))
                .reduce(StringUtils.EMPTY, (arg1, arg2) -> arg1 + arg2);
    }

    @PriorityProcessing(Priority.HIGH)
    private static class CommonHelpArgument extends AbstractCommandArgument {

        public CommonHelpArgument(Command parent, DiscordTranslator translator) {
            super(parent, "\\s+help", translator);
        }

        @Override
        public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
            return interaction.getChannel()
                    .flatMap(channel -> channel.createMessage(getParent().moreHelp(language)))
                    .flatMapMany(Flux::just);
        }

        @Override
        public String help(Language lg) {
            return "`" + getParent().getName() + " help` : " + translator.getLabel(lg, "lambda.help");
        }
    }
}
