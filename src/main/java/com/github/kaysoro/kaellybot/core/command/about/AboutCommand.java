package com.github.kaysoro.kaellybot.core.command.about;

import com.github.kaysoro.kaellybot.core.command.model.CommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.util.Translator;
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

    public AboutCommand(@Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, Translator translator) {
        super(COMMAND_NAME, arguments, translator);
    }
}