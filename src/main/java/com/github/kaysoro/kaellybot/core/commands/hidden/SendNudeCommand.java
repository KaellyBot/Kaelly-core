package com.github.kaysoro.kaellybot.core.commands.hidden;

import com.github.kaysoro.kaellybot.core.commands.arguments.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import com.github.kaysoro.kaellybot.core.model.constants.Nude;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import org.springframework.stereotype.Component;

@Component
public class SendNudeCommand extends AbstractCommand {

    public SendNudeCommand() {
        super("sendnude");
        setHidden(true);

        getArguments().add(new BasicCommandArgument(this,
                message -> message.getChannel()
                        .flatMap(chan -> {
                            if (isChannelAppropriate(chan))
                                return chan.createEmbed(spec -> spec
                                        .setTitle(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "sendnude.title"))
                                        .setFooter(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "sendnude.author")
                                                .replace("{author}", Nude.MOAM.getAuthor())
                                                .replace("{position}", "1")
                                                .replace("{number}", "1"), null)
                                        .setImage(Nude.MOAM.getImage())
                                        .setColor(Constants.COLOR));
                            else
                                return chan.createMessage(Translator
                                        .getLabel(Constants.DEFAULT_LANGUAGE, "sendnude.wrongChan"));
                        })
                        .subscribe()));
    }

    private boolean isChannelAppropriate(MessageChannel channel){
        return channel instanceof TextChannel && ((TextChannel) channel).isNsfw()
            || channel instanceof PrivateChannel;
    }
}