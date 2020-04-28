package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.argument.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.springframework.stereotype.Component;

@Component
public class InviteCommand extends AbstractCommand {

    private static final String NAME = "{name}";

    public InviteCommand() {
        super("invite");

        getArguments().add(new BasicCommandArgument(this,
                message -> message.getChannel()
                        .flatMap(chan -> chan.createEmbed(spec -> spec.setTitle(Translator
                                .getLabel(Constants.DEFAULT_LANGUAGE, "about.title")
                                .replace(NAME, Constants.NAME)
                                .replace("{version}", Constants.VERSION))
                                .setDescription(Translator
                                        .getLabel(Constants.DEFAULT_LANGUAGE, "about.desc")
                                        .replace("{game}", Constants.GAME.getName()))
                                .setColor(Constants.COLOR)
                                .setThumbnail(Constants.AVATAR)
                                .addField(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.invite.title"), 
                                        Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.invite.desc")
                                                .replace(NAME, Constants.NAME)
                                                .replace("{invite}", Constants.INVITE), true)
                                .addField(Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.support.title"),
                                        Translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.support.desc")
                                                .replace(NAME, Constants.NAME)
                                                .replace("{discordInvite}", Constants.DISCORD_INVITE), true)))
                        .subscribe()));
    }
}