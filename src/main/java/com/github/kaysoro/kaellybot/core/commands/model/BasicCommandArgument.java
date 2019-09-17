package com.github.kaysoro.kaellybot.core.commands.model;

import discord4j.core.object.entity.Message;

import java.util.function.Consumer;

public class BasicCommandArgument extends AbstractCommandArgument {

    private static final String VOID_MESSAGE = "";
    private Consumer<Message> executor;

    public BasicCommandArgument(Command parent, String subPattern, boolean isDescribed, Consumer<Message> executor){
        super(parent, subPattern, isDescribed);
        this.executor = executor;
    }

    public BasicCommandArgument(Command parent, String subPattern, Consumer<Message> executor){
        this(parent, subPattern, true, executor);
    }

    public BasicCommandArgument(Command parent, Consumer<Message> executor){
        this(parent, VOID_MESSAGE, false, executor);
    }

    @Override
    public void execute(Message message){
        executor.accept(message);
    }
}
