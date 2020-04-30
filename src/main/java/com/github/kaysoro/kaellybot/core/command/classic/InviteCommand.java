package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.argument.model.BasicCommandArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.springframework.stereotype.Component;

@Component
public class InviteCommand extends AbstractCommand {

    public InviteCommand(Translator translator) {
        super("invite", translator);

        getArguments().add(new BasicCommandArgument(this, translator,
                message -> message.getChannel()
                        .flatMap(chan -> chan.createEmbed(spec -> spec
                                .setTitle(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.title",
                                        Constants.NAME, Constants.VERSION))
                                .setDescription(translator
                                        .getLabel(Constants.DEFAULT_LANGUAGE, "about.desc", Constants.GAME.getName()))
                                .setColor(Constants.COLOR)
                                .setThumbnail(Constants.AVATAR)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.invite.title"), 
                                        translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.invite.desc",
                                                Constants.NAME, Constants.INVITE), true)
                                .addField(translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.support.title"),
                                        translator.getLabel(Constants.DEFAULT_LANGUAGE, "about.support.desc",
                                                Constants.NAME, Constants.DISCORD_INVITE), true)))
                        .subscribe()));
    }
}