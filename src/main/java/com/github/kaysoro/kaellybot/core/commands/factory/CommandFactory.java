package com.github.kaysoro.kaellybot.core.commands.factory;

import com.github.kaysoro.kaellybot.core.commands.classic.*;
import com.github.kaysoro.kaellybot.core.commands.model.Command;
import com.github.kaysoro.kaellybot.core.commands.model.SendNudeCommand;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandFactory {

    private List<Command> commands;

    public CommandFactory(){
        commands = new ArrayList<>();
        commands.add(new AboutCommand());
        commands.add(new DonateCommand());
        commands.add(new HelpCommand(this));
        commands.add(new InviteCommand());
        commands.add(new PingCommand());
        commands.add(new SendNudeCommand());
    }

    public List<Command> getCommands(){
        return commands;
    }
}
