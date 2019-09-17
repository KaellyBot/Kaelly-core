package com.github.kaysoro.kaellybot.core.commands.factory;

import com.github.kaysoro.kaellybot.core.commands.classic.HelpCommand;
import com.github.kaysoro.kaellybot.core.commands.classic.PingCommand;
import com.github.kaysoro.kaellybot.core.commands.model.Command;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandFactory {

    private List<Command> commands;

    public CommandFactory(){
        commands = new ArrayList<>();
        commands.add(new HelpCommand(this));
        commands.add(new PingCommand());
    }

    public List<Command> getCommands(){
        return commands;
    }
}
