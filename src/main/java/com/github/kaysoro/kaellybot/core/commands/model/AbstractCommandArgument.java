package com.github.kaysoro.kaellybot.core.commands.model;

import com.github.kaysoro.kaellybot.core.model.constants.DiscordConstants;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import discord4j.core.object.entity.Message;

public abstract class AbstractCommandArgument implements CommandArgument {

    private final String VOID_MESSAGE = "";
    protected Command parent;
    protected String pattern;

    public AbstractCommandArgument(Command parent, String subPattern){
        super();
        this.parent = parent;
        this.pattern = DiscordConstants.DEFAULT_PREFIX + parent.getName() + subPattern;
    }

    @Override
    public boolean triggerMessage(Message message) {
        System.out.println(message.getContent().orElse(""));
        return message.getContent().orElse(VOID_MESSAGE).matches(pattern);
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + parent.getName();
    }
}
