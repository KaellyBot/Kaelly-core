package com.github.kaysoro.kaellybot.core.command.hidden;

import com.github.kaysoro.kaellybot.core.command.argument.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Nude;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.stereotype.Component;

@Component
public class SendNudeCommand extends AbstractCommand {

    public SendNudeCommand(Translator translator) {
        super("sendnude", translator);
        setHidden(true);

        getArguments().add(new BasicCommandArgument(this, translator,
                message -> message.getChannel()
                        .flatMap(chan -> {
                            if (isChannelAppropriate(chan))
                                return chan.createEmbed(spec -> spec
                                        .setTitle(translator.getLabel(Constants.DEFAULT_LANGUAGE, "sendnude.title"))
                                        .setFooter(translator.getLabel(Constants.DEFAULT_LANGUAGE, "sendnude.author",
                                                Nude.MOAM.getAuthor(), "1", "1"), null)
                                        .setImage(Nude.MOAM.getImage())
                                        .setColor(Constants.COLOR));
                            else
                                return chan.createMessage(translator
                                        .getLabel(Constants.DEFAULT_LANGUAGE, "sendnude.wrongChan"));
                        })
                        .subscribe()));
    }

    private boolean isChannelAppropriate(MessageChannel channel){
        return channel instanceof TextChannel && ((TextChannel) channel).isNsfw()
            || channel instanceof PrivateChannel;
    }
}