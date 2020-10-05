package com.github.kaellybot.core.command.util;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.model.error.ErrorFactory;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.model.constant.PermissionScope;
import com.github.kaellybot.core.model.constant.Priority;
import com.github.kaellybot.core.util.annotation.PriorityProcessing;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.util.PermissionSet;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
public abstract class AbstractCommand implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

    protected String name;
    protected List<CommandArgument<Message>> arguments;
    private boolean isPublic;
    private boolean isAdmin;
    private boolean isHidden;
    protected DiscordTranslator translator;

    protected AbstractCommand(String name, List<CommandArgument<Message>> arguments, DiscordTranslator translator){
        super();
        this.name = name;
        this.isPublic = true;
        this.isAdmin = false;
        this.isHidden = false;
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
    public final Flux<Message> request(Message message, String prefix, Language language) {
        return getPermissions(message)
                .flatMapMany(permissions -> Flux.fromIterable(arguments)
                        .filter(argument -> argument.triggerMessage(message, prefix))
                        .sort().take(1)
                        .flatMap(argument -> argument.tryExecute(message, prefix, language, permissions))
                        .switchIfEmpty(manageMisusedCommandError(message, prefix, language, permissions)));
    }

    private Flux<Message> manageMisusedCommandError(Message message, String prefix, Language language, PermissionSet permissions){
        return Flux.just(message.getContent().startsWith(prefix + getName()))
                .filter(Boolean.TRUE::equals)
                .flatMap(result -> sendException(message, language, permissions,
                        ErrorFactory.createMisusedCommandError(prefix, this)));
    }

    public Flux<Message> sendException(Message message, Language language, PermissionSet permissions, Error error){
        return message.getChannel()
                .filter(channel -> permissions.containsAll(PermissionScope.TEXT_PERMISSIONS.getPermissions()))
                .flatMapMany(channel -> channel.createMessage(translator.getLabel(language, error)))
                .switchIfEmpty(message.getAuthor().map(User::getPrivateChannel).orElseGet(Mono::empty)
                        .flatMapMany(channel -> channel.createMessage(translator.getLabel(Constants.DEFAULT_LANGUAGE, error))))
                .onErrorResume(ClientException.isStatusCode(403), err -> Mono.empty());
    }

    private Mono<PermissionSet> getPermissions(Message message){
        return message.getChannel()
                .filter(channel -> channel instanceof TextChannel)
                .map(TextChannel.class::cast)
                .flatMap(channel -> channel.getEffectivePermissions(message.getClient().getSelfId()));
    }

    @Override
    public String help(Language lg, String prefix){
        return "**" + prefix + name + "** " + translator.getLabel(lg, name.toLowerCase() + ".help");
    }

    @Override
    public String moreHelp(Language lg, String prefix){
        return help(lg, prefix) + arguments.stream().filter(CommandArgument::isDescribed)
                .map(arg -> "\n" + arg.help(lg, prefix))
                .reduce(StringUtils.EMPTY, (arg1, arg2) -> arg1 + arg2);
    }

    @PriorityProcessing(Priority.HIGH)
    private static class CommonHelpArgument extends AbstractCommandArgument {

        public CommonHelpArgument(Command parent, DiscordTranslator translator) {
            super(parent, "\\s+help", false, translator);
        }

        @Override
        public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
            return message.getChannel()
                    .flatMap(channel -> channel.createMessage(getParent().moreHelp(language, prefix)))
                    .flatMapMany(Flux::just);
        }

        @Override
        public String help(Language lg, String prefix) {
            return prefix + "`" + getParent().getName() + " help` : " + translator.getLabel(lg, "lambda.help");
        }
    }
}
