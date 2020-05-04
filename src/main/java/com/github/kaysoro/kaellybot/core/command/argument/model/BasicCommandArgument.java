package com.github.kaysoro.kaellybot.core.command.argument.model;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Permission;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;

public abstract class BasicCommandArgument extends AbstractCommandArgument {

    private static final String VOID_MESSAGE = "";
    private Function<Message, Flux<Message>> executor;

    private BasicCommandArgument(Command parent, String subPattern, boolean isDescribed, Translator translator,
                                 Set<Permission> permissions, Function<Message, Flux<Message>> executor){
        super(parent, subPattern, isDescribed, permissions, translator);
        this.executor = executor;
    }

    public BasicCommandArgument(Command parent, Translator translator, Set<Permission> permissions, Function<Message, Flux<Message>> executor){
        this(parent, VOID_MESSAGE, false, translator, permissions, executor);
    }

    @Override
    public Flux<Message> execute(Message message){
        return executor.apply(message);
    }

    @Override
    public Flux<Message> execute(Message message, Matcher matcher){
        return execute(message);
    }
}
