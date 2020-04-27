package com.github.kaysoro.kaellybot.core.commands.classic;

import com.github.kaysoro.kaellybot.core.commands.arguments.help.HelpArgument;
import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.commands.arguments.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.commands.model.Command;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import com.github.kaysoro.kaellybot.core.util.Translator;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Getter
public class HelpCommand extends AbstractCommand {

    private List<Command> commands;

    public HelpCommand(List<Command> commands){
        super("help");
        this.commands = commands;
        this.commands.add(this);
        this.commands.sort(Comparator.comparing(Command::getName));

        getArguments().add(new BasicCommandArgument(this,
                message -> message.getChannel().flatMap(chan -> chan.createMessage(
                        commands.stream()
                                .filter(command -> command.isPublic() && ! command.isAdmin() && ! command.isHidden())
                                .map(command -> command.help(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_PREFIX))
                                .reduce((cmd1, cmd2) -> cmd1 + "\n" + cmd2)
                                .orElse(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "help.empty"))))
                        .subscribe()
        ));

        getArguments().add(new HelpArgument(this));
    }
}