package com.github.kaellybot.core.command.util;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.constant.Order;
import com.github.kaellybot.core.model.constant.Priority;
import com.github.kaellybot.core.model.error.ErrorFactory;
import com.github.kaellybot.core.util.annotation.*;
import com.github.kaellybot.core.util.DiscordTranslator;
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

    public AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed, DiscordTranslator translator){
        super();
        this.parent = parent;
        this.pattern = parent.getName() + subPattern;
        this.isDescribed = isDescribed;
        this.translator = translator;
        this.botPermissions = this.getClass().getAnnotation(BotPermissions.class).value().getPermissions();
        this.userPermissions = this.getClass().getAnnotation(UserPermissions.class).value().getPermissions();
        this.order = this.getClass().getAnnotation(DisplayOrder.class).value();
        this.priority = this.getClass().getAnnotation(PriorityProcessing.class).value();
        this.isNSFW = this.getClass().isAnnotationPresent(NSFW.class);
    }

    public AbstractCommandArgument(Command parent, boolean isDescribed, DiscordTranslator translator){
        this(parent, StringUtils.EMPTY, isDescribed, translator);
    }

    public AbstractCommandArgument(Command parent, DiscordTranslator translator){
        this(parent, StringUtils.EMPTY, false, translator);
    }

    @Override
    public boolean triggerMessage(Message message, String prefix) {
        return message.getContent().matches(Pattern.quote(prefix) + pattern);
    }

    @Override
    public boolean isArgumentHasPermissionsNeeded(PermissionSet permissions){
        return permissions.containsAll(this.botPermissions);
    }

    @Override
    public boolean isUserHasPermissionsNeeded(PermissionSet permissions){
        return permissions.containsAll(this.userPermissions);
    }

    @Override
    public boolean isChannelNSFWCompatible(MessageChannel channel){
        return !isNSFW() || (channel instanceof TextChannel && ((TextChannel) channel).isNsfw()
                || channel instanceof PrivateChannel);
    }

    @Override
    public Flux<Message> tryExecute(Message message, String prefix, Language language, PermissionSet botPermission,
                                    PermissionSet userPermission){
        return Mono.just(isArgumentHasPermissionsNeeded(botPermission))
                .flatMapMany(hasPermissions -> Boolean.TRUE.equals(hasPermissions) ?
                        Flux.empty() : getParent().sendException(message, language, botPermission,
                        ErrorFactory.createMissingBotPermissionError(getParent(), botPermissions)))
                .switchIfEmpty(message.getChannel().flatMapMany(channel -> isUserHasPermissionsNeeded(userPermission) ?
                        Flux.empty() : getParent().sendException(message, language, botPermission,
                        ErrorFactory.createMissingUserPermissionError(getParent(), userPermissions))))
                .switchIfEmpty(message.getChannel().flatMapMany(channel -> isChannelNSFWCompatible(channel) ?
                        Flux.empty() : getParent().sendException(message, language, botPermission,
                        ErrorFactory.createMissingNSFWOptionError())))
                .switchIfEmpty(execute(message, prefix, language, botPermission));
    }

    private Flux<Message> execute(Message message, String prefix, Language language, PermissionSet permissions){
        return Mono.just(Pattern.compile(Pattern.quote(prefix) + pattern).matcher(message.getContent()))
                .filter(Matcher::matches)
                .flatMapMany(matcher -> execute(message, prefix, language, matcher))
                .onErrorResume(error -> manageUnknownException(message, error))
                .switchIfEmpty(getParent().sendException(message, language, permissions, ErrorFactory.createUnknownError()));
    }

    public abstract Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher);

    protected Flux<Message> manageUnknownException(Message message, Throwable error){
        LOG.error("Error with the following command: {}", message.getContent(), error);
        return Flux.empty();
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + parent.getName();
    }

    @Override
    public boolean isDescribed(){
        return isDescribed;
    }

    @Override
    public Priority getPriority(){
        return priority;
    }

    @Override
    public Order getOrder(){
        return order;
    }

    protected Command getParent(){
        return parent;
    }
}