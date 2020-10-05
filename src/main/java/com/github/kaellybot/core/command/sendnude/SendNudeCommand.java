package com.github.kaellybot.core.command.sendnude;

import com.github.kaellybot.core.command.util.AbstractCommand;
import com.github.kaellybot.core.command.util.CommandArgument;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier(SendNudeCommand.COMMAND_QUALIFIER)
public class SendNudeCommand extends AbstractCommand {

    public static final String COMMAND_QUALIFIER = "SendNudeCommand";
    public static final String COMMAND_NAME = "sendnude";

    public SendNudeCommand(@Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, DiscordTranslator translator) {
        super(COMMAND_NAME, arguments, translator);
        setHidden(true);
    }
}