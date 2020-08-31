package com.github.kaysoro.kaellybot.core.command.server;

import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.command.model.CommandArgument;
import com.github.kaysoro.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier(ServerCommand.COMMAND_QUALIFIER)
public class ServerCommand extends AbstractCommand {

    public static final String COMMAND_QUALIFIER = "ServerCommand";
    public static final String COMMAND_NAME = "server";

    public ServerCommand(@Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, DiscordTranslator translator) {
        super(COMMAND_NAME, arguments, translator);
    }
}