package com.github.kaysoro.kaellybot.core.command.help;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(HelpCommand.COMMAND_QUALIFIER)
public class HelpArgument extends AbstractCommandArgument {

    public HelpArgument(@Qualifier(HelpCommand.COMMAND_QUALIFIER) Command parent, Translator translator){
        super(parent, "\\s+(.+)", true, PermissionScope.TEXT_PERMISSIONS, translator);
    }

    @Override
    public Flux<Message> execute(Message message, Matcher matcher) {
        String argument = matcher.group(1);
        return (! argument.equals(getParent().getName())) ?
                message.getChannel()
                        .zipWith(translator.getLanguage(message))
                        .flatMap(tuple -> tuple.getT1().createMessage(getParent().getCommands().stream()
                                .filter(cmd -> cmd.getName().equals(argument))
                                .findFirst().map(cmd -> cmd.moreHelp(tuple.getT2(), Constants.DEFAULT_PREFIX))
                                .orElse(translator.getLabel(tuple.getT2(), "help.cmd.empty"))))
                        .flatMapMany(Flux::just)
                : Flux.empty();
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