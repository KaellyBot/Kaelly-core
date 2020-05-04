package com.github.kaysoro.kaellybot.core.command.argument.model;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Permission;
import discord4j.rest.util.PermissionSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCommandArgument implements CommandArgument {

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
    public boolean isArgumentHasPermissionsNeeded(PermissionSet permissions){
        return permissions.containsAll(this.permissions);
    }

    @Override
    public boolean triggerMessage(Message message) {
        return message.getContent().matches(pattern);
    }

    @Override
    public void execute(Message message){
        Matcher matcher = Pattern.compile(pattern).matcher(message.getContent());
        if (matcher.matches())
            execute(message, matcher);
        else
            message.getChannel().flatMap(channel -> channel
                .createMessage(translator.getLabel(Constants.DEFAULT_LANGUAGE, "exception.unknown")))
            .subscribe();
    }

    protected void manageUnknownException(Message message, Throwable error){
        LOG.error("Error with the following call: {}", message.getContent(), error);
        message.getChannel().flatMap(channel -> channel.createMessage(
                translator.getLabel(Constants.DEFAULT_LANGUAGE,"exception.unknown")))
                .subscribe();
    }

    public abstract void execute(Message message, Matcher matcher);

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