package com.github.kaysoro.kaellybot.core.command.argument.help;

import com.github.kaysoro.kaellybot.core.command.classic.HelpCommand;
import com.github.kaysoro.kaellybot.core.command.argument.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;

import java.util.regex.Matcher;

public class HelpArgument extends AbstractCommandArgument {

    public HelpArgument(HelpCommand parent, Translator translator){
        super(parent, "\\s+(.+)", true, PermissionScope.TEXT_PERMISSIONS, translator);
    }

    @Override
    public void execute(Message message, Matcher matcher) {
        String argument = matcher.group(1);
        if (! argument.equals(getParent().getName()))
            message.getChannel().flatMap(channel -> channel
                    .createMessage(getParent().getCommands().stream()
                            .filter(cmd -> cmd.getName().equals(argument))
                            .findFirst().map(cmd ->
                                    cmd.moreHelp(Constants.DEFAULT_LANGUAGE, Constants.DEFAULT_PREFIX))
                            .orElse(translator.getLabel(Constants.DEFAULT_LANGUAGE, "help.cmd.empty"))))
                    .subscribe();
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " commandName` : "
                + translator.getLabel(lg, "help.cmd");
    }

    @Override
    protected HelpCommand getParent(){
        return (HelpCommand) super.getParent();
    }
}