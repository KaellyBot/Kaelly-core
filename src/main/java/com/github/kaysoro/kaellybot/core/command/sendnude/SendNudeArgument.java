package com.github.kaysoro.kaellybot.core.command.sendnude;

import com.github.kaysoro.kaellybot.core.command.model.EmbedCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Nude;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.Color;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.regex.Matcher;

@Component
@Qualifier(SendNudeCommand.COMMAND_QUALIFIER)
public class SendNudeArgument extends EmbedCommandArgument {

    public SendNudeArgument(@Qualifier(SendNudeCommand.COMMAND_QUALIFIER) Command parent, Translator translator) {
        super(parent, translator);
        setNSFW(true);
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Matcher matcher) {
        return message.getChannel()
                .zipWith(translator.getLanguage(message))
                .flatMap(tuple -> tuple.getT1().createEmbed(spec -> spec
                        .setTitle(translator.getLabel(tuple.getT2(), "sendnude.title"))
                        .setFooter(translator.getLabel(tuple.getT2(), "sendnude.author",
                                Nude.MOAM.getAuthor(), "1", "1"), null)
                        .setImage(Nude.MOAM.getImage())
                        .setColor(Color.of(16738740))))
                .flatMapMany(Flux::just);
    }
}