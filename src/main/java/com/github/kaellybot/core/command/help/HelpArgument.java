package com.github.kaellybot.core.command.help;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Described
@Component
@Qualifier(HelpCommand.COMMAND_QUALIFIER)
public class HelpArgument extends AbstractCommandArgument {

    public HelpArgument(@Qualifier(HelpCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator){
        super(parent, "\\s+(.+)", translator);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        String argument = matcher.group(1);
        return (! argument.equals(getParent().getName())) ?
                message.getChannel()
                        .flatMap(channel -> channel.createMessage(getParent().getCommands().stream()
                                .filter(cmd -> cmd.getName().equals(argument))
                                .findFirst().map(cmd -> cmd.moreHelp(language, prefix))
                                .orElse(translator.getLabel(language, "help.cmd.empty"))))
                        .flatMapMany(Flux::just)
                : Flux.empty();
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " commandName` : "
                + translator.getLabel(lg, "help.cmd");
    }

    @Override
    public HelpCommand getParent(){
        return (HelpCommand) super.getParent();
    }
}