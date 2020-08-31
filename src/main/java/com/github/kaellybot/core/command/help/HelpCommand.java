package com.github.kaellybot.core.command.help;

import com.github.kaellybot.core.command.model.AbstractCommand;
import com.github.kaellybot.core.command.model.Command;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.command.model.CommandArgument;
import discord4j.core.object.entity.Message;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Getter
@Qualifier(HelpCommand.COMMAND_QUALIFIER)
public class HelpCommand extends AbstractCommand {

    public static final String COMMAND_QUALIFIER = "HelpCommand";
    public static final String COMMAND_NAME = "help";

    private final List<Command> commands;

    public HelpCommand(List<Command> commands, @Qualifier(COMMAND_QUALIFIER) @Lazy List<CommandArgument<Message>> arguments, DiscordTranslator translator){
        super(COMMAND_NAME, arguments, translator);
        this.commands = commands;
        this.commands.add(this);
        this.commands.sort(Comparator.comparing(Command::getName));
    }
}