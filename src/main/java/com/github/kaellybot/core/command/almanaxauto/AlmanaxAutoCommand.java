package com.github.kaellybot.core.command.almanaxauto;

import com.github.kaellybot.core.command.model.AbstractCommand;
import com.github.kaellybot.core.command.model.CommandArgument;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier(AlmanaxAutoCommand.COMMAND_QUALIFIER)
public class AlmanaxAutoCommand extends AbstractCommand {

    public static final String COMMAND_QUALIFIER = "AlmanaxAutoCommand";
    public static final String COMMAND_NAME = "almanax-auto";

    public AlmanaxAutoCommand(@Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, DiscordTranslator translator) {
        super(COMMAND_NAME, arguments, translator);
    }
}