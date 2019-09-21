package com.github.kaysoro.kaellybot.core.commands.arguments.model;

import com.github.kaysoro.kaellybot.core.model.constants.Language;
import discord4j.core.object.entity.Message;

public interface CommandArgument {

    boolean triggerMessage(Message message);

    void execute(Message message);

    String help(Language lg, String prefix);

    boolean isDescribed();
}
