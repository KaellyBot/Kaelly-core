package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.argument.help.HelpArgument;
import com.github.kaysoro.kaellybot.core.command.argument.model.TextCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.util.Translator;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Getter
public class HelpCommand extends AbstractCommand {

    private List<Command> commands;

    public HelpCommand(List<Command> commands, Translator translator){
        super("help", translator);
        this.commands = commands;
        this.commands.add(this);
        this.commands.sort(Comparator.comparing(Command::getName));

        getArguments().add(new TextCommandArgument(this, translator,
                message -> message.getChannel().flatMap(chan -> chan.createMessage(
                        commands.stream()
                                .filter(command -> command.isPublic() && ! command.isAdmin() && ! command.isHidden())
                                .map(command -> command.help(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_PREFIX))
                                .reduce((cmd1, cmd2) -> cmd1 + "\n" + cmd2)
                                .orElse(translator.getLabel(Constants.DEFAULT_LANGUAGE, "help.empty"))))
                        .subscribe()
        ));

        getArguments().add(new HelpArgument(this, translator));
    }
}