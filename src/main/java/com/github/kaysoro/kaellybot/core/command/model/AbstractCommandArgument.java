package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.User;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.http.client.ClientException;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractCommandArgument implements CommandArgument<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommandArgument.class);
    protected final Translator translator;
    private final Command parent;
    private final String pattern;
    private final boolean isDescribed;
    private final Set<Permission> permissions;
    private final Priority priority;

    public AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed, Set<Permission> permissions,
                                   Translator translator, Priority priority){
        super();
        this.parent = parent;
        this.pattern = Constants.DEFAULT_PREFIX + parent.getName() + subPattern;
        this.isDescribed = isDescribed;
        this.permissions = permissions;
        this.translator = translator;
        this.priority = priority;
    }

    public AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed, Set<Permission> permissions,
                                   Translator translator){
        this(parent, subPattern, isDescribed, permissions, translator, Priority.NORMAL);
    }

    @Override
    public boolean triggerMessage(Message message) {
        return message.getContent().matches(pattern);
    }

    @Override
    public boolean isArgumentHasPermissionsNeeded(PermissionSet permissions){
        return permissions.containsAll(this.permissions);
    }

    @Override
    public Flux<Message> tryExecute(Message message, PermissionSet permissions){
        return isArgumentHasPermissionsNeeded(permissions) ? execute(message) :
                message.getChannel()
                        .filter(channel -> permissions.containsAll(PermissionScope.TEXT_PERMISSIONS))
                        .zipWith(translator.getLanguage(message))
                        .flatMapMany(tuple -> sendMissingPermissions(tuple.getT1(), tuple.getT2()))
                        .switchIfEmpty(message.getAuthor().map(User::getPrivateChannel).orElseGet(Mono::empty)
                                .flatMapMany(channel -> sendMissingPermissions(channel, Constants.DEFAULT_LANGUAGE)))
                        .onErrorResume(ClientException.isStatusCode(403), err -> Mono.empty());
    }

    private Flux<Message> execute(Message message){
        Matcher matcher = Pattern.compile(pattern).matcher(message.getContent());
        return matcher.matches() ? execute(message, matcher) : message.getChannel()
                .zipWith(translator.getLanguage(message)).flatMap(tuple -> tuple.getT1()
                        .createMessage(translator.getLabel(tuple.getT2(), "exception.unknown")))
                .flatMapMany(Flux::just);
    }

    private Flux<Message> sendMissingPermissions(MessageChannel channel, Language language){
        return channel.createMessage(permissions.stream()
                .map(permission -> translator.getLabel(language, "permission." + permission.name().toLowerCase()))
                .collect(Collectors.joining(", ", translator
                        .getLabel(language, "exception.missing_permission", getParent().getName()) + " ", ".")))
                .flatMapMany(Flux::just);

    }

    protected Mono<Message> manageUnknownException(Message message, Throwable error){
        LOG.error("Error with the following call: {}", message.getContent(), error);
        return message.getChannel().zipWith(translator.getLanguage(message))
                .flatMap(tuple -> tuple.getT1().createMessage(translator
                        .getLabel(tuple.getT2(),"exception.unknown")));
    }

    public abstract Flux<Message> execute(Message message, Matcher matcher);

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