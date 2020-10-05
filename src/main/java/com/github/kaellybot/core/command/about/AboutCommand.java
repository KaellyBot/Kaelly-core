package com.github.kaellybot.core.command.about;

import com.github.kaellybot.core.command.util.AbstractCommand;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.command.util.CommandArgument;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier(AboutCommand.COMMAND_QUALIFIER)
public class AboutCommand extends AbstractCommand {

    public static final String COMMAND_QUALIFIER = "AboutCommand";
    public static final String COMMAND_NAME = "about";

    public AboutCommand(@Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, DiscordTranslator translator) {
        super(COMMAND_NAME, arguments, translator);
    }
}