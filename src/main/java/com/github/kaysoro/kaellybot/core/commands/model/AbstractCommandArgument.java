package com.github.kaysoro.kaellybot.core.commands.model;

import com.github.kaysoro.kaellybot.core.model.constants.DiscordConstants;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import discord4j.core.object.entity.Message;

public abstract class AbstractCommandArgument implements CommandArgument {

    protected Command parent;
    private String pattern;

    protected AbstractCommandArgument(Command parent, String subPattern){
        super();
        this.parent = parent;
        this.pattern = DiscordConstants.DEFAULT_PREFIX + parent.getName() + subPattern;
    }

    public boolean triggerMessage(Message message) {
        return message.getContent().map(content -> content.matches(pattern)).orElse(false);
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + parent.getName();
    }
}
