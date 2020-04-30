package com.github.kaysoro.kaellybot.core.command.argument.model;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;

import java.util.function.Consumer;
import java.util.regex.Matcher;

public class BasicCommandArgument extends AbstractCommandArgument {

    private static final String VOID_MESSAGE = "";
    private Consumer<Message> executor;

    private BasicCommandArgument(Command parent, String subPattern, boolean isDescribed, Translator translator, Consumer<Message> executor){
        super(parent, subPattern, isDescribed, translator);
        this.executor = executor;
    }

    public BasicCommandArgument(Command parent, Translator translator, Consumer<Message> executor){
        this(parent, VOID_MESSAGE, false, translator, executor);
    }

    @Override
    public void execute(Message message){
        executor.accept(message);
    }

    @Override
    public void execute(Message message, Matcher matcher){
        execute(message);
    }
}
