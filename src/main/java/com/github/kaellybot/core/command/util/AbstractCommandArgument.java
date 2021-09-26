package com.github.kaellybot.core.command.util;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.constant.Order;
import com.github.kaellybot.core.model.constant.Priority;
import com.github.kaellybot.core.model.error.ErrorFactory;
import com.github.kaellybot.core.util.annotation.*;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.command.ApplicationCommandInteraction;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.kaellybot.core.model.constant.Order.FIRST;
import static com.github.kaellybot.core.model.constant.PermissionScope.*;
import static com.github.kaellybot.core.model.constant.Priority.NORMAL;

@Getter
@DisplayOrder(FIRST)
@PriorityProcessing(NORMAL)
@BotPermissions(TEXT_PERMISSIONS)
@UserPermissions(MEMBER_PERMISSIONS)
public abstract class AbstractCommandArgument implements CommandArgument<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommandArgument.class);
    protected final DiscordTranslator translator;
    private final Command parent;
    private final String pattern;
    private final boolean isDescribed;
    private final Set<Permission> botPermissions;
    private final Set<Permission> userPermissions;
    private final Order order;
    private final Priority priority;
    private final boolean isNSFW;

    protected AbstractCommandArgument(Command parent, String subPattern, DiscordTranslator translator) {
        super();
        this.parent = parent;
        this.pattern = parent.getName() + subPattern;
        this.translator = translator;
        this.botPermissions = this.getClass().getAnnotation(BotPermissions.class).value().getPermissions();
        this.userPermissions = this.getClass().getAnnotation(UserPermissions.class).value().getPermissions();
        this.order = this.getClass().getAnnotation(DisplayOrder.class).value();
        this.priority = this.getClass().getAnnotation(PriorityProcessing.class).value();
        this.isDescribed = this.getClass().isAnnotationPresent(Described.class);
        this.isNSFW = this.getClass().isAnnotationPresent(NSFW.class);
    }

    protected AbstractCommandArgument(Command parent, DiscordTranslator translator) {
        this(parent, StringUtils.EMPTY, translator);
    }

    @Override
    public boolean triggerInteraction(Interaction interaction) {
        return interaction.getMessage().map(message -> message.getContent().matches(pattern)).orElse(false);
    }

    @Override
    public boolean isArgumentHasPermissionsNeeded(PermissionSet permissions) {
        return permissions.containsAll(this.botPermissions);
    }

    @Override
    public boolean isUserHasPermissionsNeeded(PermissionSet permissions) {
        return permissions.containsAll(this.userPermissions);
    }

    @Override
    public boolean isChannelNSFWCompatible(MessageChannel channel) {
        return !isNSFW() || (channel instanceof TextChannel && ((TextChannel) channel).isNsfw()
                || channel instanceof PrivateChannel);
    }

    @Override
    public Flux<Message> tryExecute(Interaction interaction, Language language, PermissionSet botPermission,
                                    PermissionSet userPermission) {
        return Mono.just(isArgumentHasPermissionsNeeded(botPermission))
                .flatMapMany(hasPermissions -> Boolean.TRUE.equals(hasPermissions) ?
                        Flux.empty() : getParent().sendException(interaction, language, botPermission,
                        ErrorFactory.createMissingBotPermissionError(getParent(), botPermissions)))
                .switchIfEmpty(interaction.getChannel().flatMapMany(channel -> isUserHasPermissionsNeeded(userPermission) ?
                        Flux.empty() : getParent().sendException(interaction, language, botPermission,
                        ErrorFactory.createMissingUserPermissionError(getParent(), userPermissions))))
                .switchIfEmpty(interaction.getChannel().flatMapMany(channel -> isChannelNSFWCompatible(channel) ?
                        Flux.empty() : getParent().sendException(interaction, language, botPermission,
                        ErrorFactory.createMissingNSFWOptionError())))
                .switchIfEmpty(execute(interaction, language, botPermission));
    }

    private Flux<Message> execute(Interaction interaction, Language language, PermissionSet permissions) {
        return Mono.just(Pattern.compile(pattern).matcher(interaction.getMessage().map(Message::getContent).orElse(StringUtils.EMPTY)))
                .filter(Matcher::matches)
                .flatMapMany(matcher -> execute(interaction, language, matcher))
                .onErrorResume(error -> manageUnknownException(interaction, error))
                .switchIfEmpty(getParent().sendException(interaction, language, permissions, ErrorFactory.createUnknownError()));
    }

    public abstract Flux<Message> execute(Interaction interaction, Language language, Matcher matcher);

    protected Flux<Message> manageUnknownException(Interaction interaction, Throwable error) {
        LOG.error("Error with the following command: {}", interaction.getCommandInteraction()
                .flatMap(ApplicationCommandInteraction::getName), error);
        return Flux.empty();
    }

    @Override
    public String help(Language lg) {
        return parent.getName();
    }
}