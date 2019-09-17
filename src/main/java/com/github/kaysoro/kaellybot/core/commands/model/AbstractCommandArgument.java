package com.github.kaysoro.kaellybot.core.commands.model;

import com.github.kaysoro.kaellybot.core.model.constants.DiscordConstants;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import discord4j.core.object.entity.Message;

public abstract class AbstractCommandArgument implements CommandArgument {

    private Command parent;
    private String pattern;
    private boolean isDescribed;

    AbstractCommandArgument(Command parent, String subPattern, boolean isDescribed){
        super();
        this.parent = parent;
        this.pattern = DiscordConstants.DEFAULT_PREFIX + parent.getName() + subPattern;
        this.isDescribed = isDescribed;
    }

    AbstractCommandArgument(Command parent, String subPattern){
        this(parent, subPattern, true);
    }

    @Override
    public boolean triggerMessage(Message message) {
        return message.getContent().map(content -> content.matches(pattern)).orElse(false);
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + parent.getName();
    }

    @Override
    public boolean isDescribed(){
        return isDescribed;
    }

    Command getParent(){
        return parent;
    }
}