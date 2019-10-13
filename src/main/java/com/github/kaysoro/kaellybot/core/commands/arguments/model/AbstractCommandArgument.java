package com.github.kaysoro.kaellybot.core.commands.arguments.model;

import com.github.kaysoro.kaellybot.core.commands.model.Command;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractCommandArgument implements CommandArgument {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommandArgument.class);
    private static final String VOID_MESSAGE = "";
    private Command parent;
    private String pattern;
    private boolean isDescribed;

    public AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed){
        super();
        this.parent = parent;
        this.pattern = Constants.DEFAULT_PREFIX + parent.getName() + subPattern;
        this.isDescribed = isDescribed;
    }

    @Override
    public boolean triggerMessage(Message message) {
        return message.getContent().map(content -> content.matches(pattern)).orElse(false);
    }

    @Override
    public void execute(Message message){
        Matcher matcher = Pattern.compile(pattern).matcher(message.getContent().orElse(VOID_MESSAGE));
        if (matcher.matches())
            execute(message, matcher);
        else
            message.getChannel().flatMap(channel -> channel
                .createMessage(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "exception.unknown")))
            .subscribe();
    }

    protected void manageUnknownException(Message message, Throwable error){
        LOG.error("Error with the following call: " + message.getContent().orElse(VOID_MESSAGE), error);
        message.getChannel().flatMap(channel -> channel.createMessage(
                Translator.getLabel(Constants.DEFAULT_LANGUAGE,"exception.unknown")))
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