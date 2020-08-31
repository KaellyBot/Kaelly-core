package com.github.kaellybot.core.command.sendnude;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.model.Command;
import com.github.kaellybot.core.model.constant.Nude;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.command.model.EmbedCommandArgument;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(SendNudeCommand.COMMAND_QUALIFIER)
public class SendNudeArgument extends EmbedCommandArgument {

    public SendNudeArgument(@Qualifier(SendNudeCommand.COMMAND_QUALIFIER) Command parent, DiscordTranslator translator) {
        super(parent, translator);
        setNSFW(true);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return message.getChannel()
                .flatMap(channel -> channel.createEmbed(spec -> spec
                        .setTitle(translator.getLabel(language, "sendnude.title"))
                        .setFooter(translator.getLabel(language, "sendnude.author",
                                Nude.MOAM.getAuthor(), "1", "1"), null)
                        .setImage(Nude.MOAM.getImage())
                        .setColor(Color.of(16738740))))
                .flatMapMany(Flux::just);
    }
}