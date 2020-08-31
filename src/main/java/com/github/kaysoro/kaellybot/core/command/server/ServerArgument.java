package com.github.kaysoro.kaellybot.core.command.server;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.command.model.TextCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(ServerCommand.COMMAND_QUALIFIER)
public class ServerArgument extends TextCommandArgument {

    public ServerArgument(@Qualifier(ServerCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator) {
        super(parent, translator);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return Flux.empty();
    }
}