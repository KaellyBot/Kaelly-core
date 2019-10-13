package com.github.kaysoro.kaellybot.core.commands.factory;

import com.github.kaysoro.kaellybot.core.commands.classic.*;
import com.github.kaysoro.kaellybot.core.commands.model.Command;
import com.github.kaysoro.kaellybot.core.commands.hidden.SendNudeCommand;
import com.github.kaysoro.kaellybot.core.service.PortalService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CommandFactory {

    private List<Command> commands;

    public CommandFactory(PortalService portalService){
        commands = new ArrayList<>();
        commands.add(new AboutCommand());
        commands.add(new DonateCommand());
        commands.add(new HelpCommand(this));
        commands.add(new InviteCommand());
        commands.add(new PingCommand());
        commands.add(new PortalCommand(portalService));
        commands.add(new SendNudeCommand());
    }

    public List<Command> getCommands(){
        return commands;
    }
}
