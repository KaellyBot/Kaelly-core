package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import reactor.core.publisher.Flux;

import java.util.function.Function;

public class EmbedCommandArgument extends BasicCommandArgument {

    public EmbedCommandArgument(Command parent, Translator translator, Function<Message, Flux<Message>> executor) {
        super(parent, translator, PermissionScope.EMBED_PERMISSIONS, executor);
    }
}