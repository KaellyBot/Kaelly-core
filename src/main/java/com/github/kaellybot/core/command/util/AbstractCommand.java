package com.github.kaellybot.core.command.util;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.model.error.ErrorFactory;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.model.constant.PermissionScope;
import com.github.kaellybot.core.util.annotation.Hidden;
import com.github.kaellybot.core.util.annotation.SuperAdministrator;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Member;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.discordjson.json.ImmutableApplicationCommandRequest;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.util.PermissionSet;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

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

    @Override
    public ApplicationCommandRequest getApplicationCommandRequest() {
        ImmutableApplicationCommandRequest.Builder builder = ApplicationCommandRequest.builder()
                .name(getName())
                .description(help(Constants.DEFAULT_LANGUAGE));
        return builder.build();
    }

    @Override
    public final Flux<Void> request(ApplicationCommandInteractionEvent event, Language language) {
        return getBotPermissions(event.getInteraction())
                .zipWith(getUserPermissions(event.getInteraction()))
                .flatMapMany(tuple -> Flux.fromIterable(arguments)
                        .filter(argument -> argument.triggerInteraction(event.getInteraction()))
                        .sort(Comparator.comparing(CommandArgument::getPriority))
                        .take(1)
                        .flatMap(argument -> argument.tryExecute(event.getInteraction(), language, tuple.getT1(), tuple.getT2()))
                        .thenMany(Flux.empty()));
    }

    private Flux<Void> manageMisusedCommandError(Interaction interaction, Language language, PermissionSet permissions){
        return Flux.just(interaction.getMessage().map(message -> message.getContent().startsWith(getName())).orElse(false))
                .filter(Boolean.TRUE::equals)
                .flatMap(result -> sendException(interaction, language, permissions,
                        ErrorFactory.createMisusedCommandError(this)))
                .thenMany(Flux.empty());
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
}
