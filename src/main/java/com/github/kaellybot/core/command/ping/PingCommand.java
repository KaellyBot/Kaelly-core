package com.github.kaellybot.core.command.ping;

import com.github.kaellybot.core.command.model.CommandArgument;
import com.github.kaellybot.core.command.model.AbstractCommand;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@Qualifier(PingCommand.COMMAND_QUALIFIER)
public class PingCommand extends AbstractCommand {

    public static final String COMMAND_QUALIFIER = "PingCommand";
    public static final String COMMAND_NAME = "ping";

    public PingCommand(@Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, DiscordTranslator translator) {
        super(COMMAND_NAME, arguments, translator);
    }
}