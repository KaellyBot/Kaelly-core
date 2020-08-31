package com.github.kaellybot.core.command.model;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.error.ErrorFactory;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Setter
public abstract class AbstractCommandArgument implements CommandArgument<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommandArgument.class);
    protected final DiscordTranslator translator;
    private final Command parent;
    private final String pattern;
    private final boolean isDescribed;
    private final Set<Permission> permissions;
    private Priority priority;
    private boolean isNSFW;

    public AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed, Set<Permission> permissions,
                                   DiscordTranslator translator, Priority priority){
        super();
        this.parent = parent;
        this.pattern = parent.getName() + subPattern;
        this.isDescribed = isDescribed;
        this.permissions = permissions;
        this.translator = translator;
        this.priority = priority;
        this.isNSFW = false;
    }

    public AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed, Set<Permission> permissions,
                                   DiscordTranslator translator){
        this(parent, subPattern, isDescribed, permissions, translator, Priority.NORMAL);
    }

    public AbstractCommandArgument(Command parent, boolean isDescribed, Set<Permission> permissions,
                                   DiscordTranslator translator){
        this(parent, StringUtils.EMPTY, isDescribed, permissions, translator, Priority.NORMAL);
    }

    @Override
    public boolean triggerMessage(Message message, String prefix) {
        return message.getContent().matches(Pattern.quote(prefix) + pattern);
    }

    @Override
    public boolean isArgumentHasPermissionsNeeded(PermissionSet permissions){
        return permissions.containsAll(this.permissions);
    }

    @Override
    public boolean isChannelNSFWCompatible(MessageChannel channel){
        return !isNSFW() || (channel instanceof TextChannel && ((TextChannel) channel).isNsfw()
                || channel instanceof PrivateChannel);
    }

    @Override
    public Flux<Message> tryExecute(Message message, String prefix, Language language, PermissionSet channelPermission){
        return Mono.just(isArgumentHasPermissionsNeeded(channelPermission))
                .flatMapMany(hasPermissions -> Boolean.TRUE.equals(hasPermissions) ?
                        Flux.empty() : getParent().sendException(message, language, channelPermission,
                        ErrorFactory.createMissingPermissionError(getParent(), permissions)))
                .switchIfEmpty(message.getChannel().flatMapMany(channel -> isChannelNSFWCompatible(channel) ?
                        Flux.empty() : getParent().sendException(message, language, channelPermission,
                        ErrorFactory.createMissingNSFWOptionError())))
                .switchIfEmpty(execute(message, prefix, language, channelPermission));
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

    protected Command getParent(){
        return parent;
    }

    @Override
    public int compareTo(CommandArgument<Message> argument){
        return getPriority().compareTo(argument.getPriority());
    }
}