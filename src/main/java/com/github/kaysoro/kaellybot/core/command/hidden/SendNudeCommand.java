package com.github.kaysoro.kaellybot.core.command.hidden;

import com.github.kaysoro.kaellybot.core.command.argument.model.EmbedCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constant.Nude;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class SendNudeCommand extends AbstractCommand {

    public SendNudeCommand(Translator translator) {
        super("sendnude", translator);
        setHidden(true);

        getArguments().add(new EmbedCommandArgument(this, translator,
                message -> message.getChannel()
                        .zipWith(translator.getLanguage(message))
                        .flatMap(tuple -> {
                            if (isChannelAppropriate(tuple.getT1()))
                                return tuple.getT1().createEmbed(spec -> spec
                                        .setTitle(translator.getLabel(tuple.getT2(), "sendnude.title"))
                                        .setFooter(translator.getLabel(tuple.getT2(), "sendnude.author",
                                                Nude.MOAM.getAuthor(), "1", "1"), null)
                                        .setImage(Nude.MOAM.getImage())
                                        .setColor(16738740));
                            else
                                return tuple.getT1().createMessage(translator
                                        .getLabel(tuple.getT2(), "sendnude.wrongChan"));
                        })
                        .flatMapMany(Flux::just)));
    }

    private boolean isChannelAppropriate(MessageChannel channel){
        return channel instanceof TextChannel && ((TextChannel) channel).isNsfw()
            || channel instanceof PrivateChannel;
    }
}