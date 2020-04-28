package com.github.kaysoro.kaellybot.core.trigger;

import discord4j.core.object.entity.Message;

public interface Trigger {

    boolean isTriggered(Message message);

    void execute(Message message);
}
