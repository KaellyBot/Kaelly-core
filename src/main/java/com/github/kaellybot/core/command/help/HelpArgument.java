package com.github.kaellybot.core.command.help;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import discord4j.core.object.command.Interaction;
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
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        String argument = matcher.group(1);
        return (! argument.equals(getParent().getName())) ?
                interaction.getChannel()
                        .flatMap(channel -> channel.createMessage(getParent().getCommands().stream()
                                .filter(cmd -> cmd.getName().equals(argument))
                                .findFirst().map(cmd -> cmd.moreHelp(language))
                                .orElse(translator.getLabel(language, "help.cmd.empty"))))
                        .flatMapMany(Flux::just)
                : Flux.empty();
    }

    @Override
    public String help(Language lg){
        return "`" + getParent().getName() + " commandName` : "
                + translator.getLabel(lg, "help.cmd");
    }

    @Override
    public HelpCommand getParent(){
        return (HelpCommand) super.getParent();
    }
}