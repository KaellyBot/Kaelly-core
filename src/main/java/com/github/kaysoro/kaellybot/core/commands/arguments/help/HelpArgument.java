package com.github.kaysoro.kaellybot.core.commands.arguments.help;

import com.github.kaysoro.kaellybot.core.commands.classic.HelpCommand;
import com.github.kaysoro.kaellybot.core.commands.arguments.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;

import java.util.regex.Matcher;

public class HelpArgument extends AbstractCommandArgument {

    public HelpArgument(HelpCommand parent){
        super(parent, "\\s+(.+)", true);
    }

    @Override
    public void execute(Message message, Matcher matcher) {
        String argument = matcher.group(1);
        if (! argument.equals(getParent().getName()))
            message.getChannel().flatMap(channel -> channel
                    .createMessage(getParent().getCommandFactory().getCommands().stream()
                            .filter(cmd -> cmd.getName().equals(argument))
                            .findFirst().map(cmd ->
                                    cmd.moreHelp(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_PREFIX))
                            .orElse(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "help.cmd.empty"))))
                    .subscribe();
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " commandName` : "
                + Translator.getLabel(lg, "help.cmd");
    }

    @Override
    protected HelpCommand getParent(){
        return (HelpCommand) super.getParent();
    }
}