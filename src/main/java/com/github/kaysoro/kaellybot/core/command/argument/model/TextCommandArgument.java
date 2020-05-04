package com.github.kaysoro.kaellybot.core.command.argument.model;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class TextCommandArgument extends BasicCommandArgument {

    public TextCommandArgument(Command parent, Translator translator, Function<Message, Flux<Message>> executor) {
        super(parent, translator, PermissionScope.TEXT_PERMISSIONS, executor);
    }
}
