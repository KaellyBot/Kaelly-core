package com.github.kaellybot.core.command.help;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(HelpCommand.COMMAND_QUALIFIER)
public class HelpNoArgument extends AbstractCommandArgument {

    public HelpNoArgument(HelpCommand parent, DiscordTranslator translator) {
        super(parent, translator);
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        return interaction.getChannel()
                .flatMap(channel -> channel.createMessage(getCommandList(language)))
                .flatMapMany(Flux::just);
    }

    public String getCommandList(Language language){
        return ((HelpCommand) getParent()).getCommands().stream()
                .filter(command -> ! command.isAdmin() && ! command.isHidden())
                .map(command -> command.help(language))
                .reduce((cmd1, cmd2) -> cmd1 + "\n" + cmd2)
                .orElse(translator.getLabel(language, "help.empty"));
    }
}
