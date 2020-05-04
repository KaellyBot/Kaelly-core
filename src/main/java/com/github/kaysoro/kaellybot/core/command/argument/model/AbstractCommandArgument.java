package com.github.kaysoro.kaellybot.core.command.argument.model;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public abstract class AbstractCommandArgument implements CommandArgument<Message> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommandArgument.class);
    protected Translator translator;
    private Command parent;
    private String pattern;
    private boolean isDescribed;
    private Set<Permission> permissions;

    public AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed, Set<Permission> permissions,
                                   Translator translator){
        super();
        this.parent = parent;
        this.pattern = Constants.DEFAULT_PREFIX + parent.getName() + subPattern;
        this.isDescribed = isDescribed;
        this.permissions = permissions;
        this.translator = translator;
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
                        .flatMapMany(channel -> sendMissingPermissions(channel, Constants.DEFAULT_LANGUAGE));
    }

    private Flux<Message> execute(Message message){
        Matcher matcher = Pattern.compile(pattern).matcher(message.getContent());
        return matcher.matches() ? execute(message, matcher) : message.getChannel()
                .flatMap(channel -> channel
                        .createMessage(translator.getLabel(Constants.DEFAULT_LANGUAGE, "exception.unknown")))
                .flatMapMany(Flux::just);
    }

    private Flux<Message> sendMissingPermissions(MessageChannel channel, Language language){
        return channel.createMessage(permissions.stream()
                .map(permission -> translator.getLabel(language, "permission." + permission.name().toLowerCase()))
                .collect(Collectors.joining(", ", translator
                        .getLabel(language, "exception.missing_permission", getParent().getName()) + " ", ".")))
                .flatMapMany(Flux::just);

    }

    protected void manageUnknownException(Message message, Throwable error){
        LOG.error("Error with the following call: {}", message.getContent(), error);
        message.getChannel().flatMap(channel -> channel.createMessage(
                translator.getLabel(Constants.DEFAULT_LANGUAGE,"exception.unknown")))
                .subscribe();
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

    protected Command getParent(){
        return parent;
    }
}