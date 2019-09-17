package com.github.kaysoro.kaellybot.core.commands.classic;

import com.github.kaysoro.kaellybot.core.commands.factory.CommandFactory;
import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.commands.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constants.DiscordConstants;
import com.github.kaysoro.kaellybot.core.util.Translator;

public class HelpCommand extends AbstractCommand {

    public HelpCommand(CommandFactory commandFactory){
        super("help");

        getArguments().add(new BasicCommandArgument(this,
                message -> message.getChannel().flatMap(chan -> chan.createMessage(
                        commandFactory.getCommands().stream()
                                .filter(command -> command.isPublic() && ! command.isAdmin() && ! command.isHidden())
                                .map(command -> command.help(DiscordConstants.DEFAULT_LANGUAGE, DiscordConstants.DEFAULT_PREFIX))
                                .reduce((cmd1, cmd2) -> cmd1 + "\n" + cmd2)
                                .orElse(Translator.getLabel(DiscordConstants.DEFAULT_LANGUAGE, "help.empty"))))
                        .subscribe()
        ));
    }
}