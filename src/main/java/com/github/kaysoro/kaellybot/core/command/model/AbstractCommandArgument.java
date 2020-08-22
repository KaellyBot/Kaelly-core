package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.model.error.Error;
import com.github.kaysoro.kaellybot.core.model.error.ErrorFactory;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.http.client.ClientException;
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
    protected final Translator translator;
    private final Command parent;
    private final String pattern;
    private final boolean isDescribed;
    private final Set<Permission> permissions;
    private Priority priority;
    private boolean isNSFW;

    public AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed, Set<Permission> permissions,
                                   Translator translator, Priority priority){
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
                                   Translator translator){
        this(parent, subPattern, isDescribed, permissions, translator, Priority.NORMAL);
    }

    public AbstractCommandArgument(Command parent, boolean isDescribed, Set<Permission> permissions,
                                   Translator translator){
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
    public Flux<Message> tryExecute(Message message, String prefix, PermissionSet channelPermission){
        return Mono.just(isArgumentHasPermissionsNeeded(channelPermission))
                .flatMapMany(hasPermissions -> Boolean.TRUE.equals(hasPermissions) ?
                        Flux.empty() : sendException(message, channelPermission, ErrorFactory.createMissingPermissionError(getParent(), permissions)))
                .switchIfEmpty(message.getChannel().flatMapMany(channel -> isChannelNSFWCompatible(channel) ?
                        Flux.empty() : sendException(message, channelPermission, ErrorFactory.createMissingNSFWOptionError())))
                .switchIfEmpty(execute(message, prefix, channelPermission));
    }

    private Flux<Message> execute(Message message, String prefix, PermissionSet permissions){
        return Mono.just(Pattern.compile(Pattern.quote(prefix) + pattern).matcher(message.getContent()))
                .filter(Matcher::matches)
                .flatMapMany(matcher -> execute(message, prefix, matcher))
                .onErrorResume(error -> manageUnknownException(message, error))
                .switchIfEmpty(sendException(message, permissions, ErrorFactory.createUnknownError()));
    }

    public abstract Flux<Message> execute(Message message, String prefix, Matcher matcher);

    private Flux<Message> sendException(Message message, PermissionSet permissions, Error error){
        return message.getChannel()
                .filter(channel -> permissions.containsAll(PermissionScope.TEXT_PERMISSIONS))
                .zipWith(translator.getLanguage(message))
                .flatMapMany(tuple -> tuple.getT1().createMessage(translator.getLabel(tuple.getT2(), error)))
                .switchIfEmpty(message.getAuthor().map(User::getPrivateChannel).orElseGet(Mono::empty)
                        .flatMapMany(channel -> channel.createMessage(translator.getLabel(Constants.DEFAULT_LANGUAGE, error))))
                .onErrorResume(ClientException.isStatusCode(403), err -> Mono.empty());
    }

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