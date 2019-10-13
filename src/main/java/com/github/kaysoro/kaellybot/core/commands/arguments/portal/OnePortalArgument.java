package com.github.kaysoro.kaellybot.core.commands.arguments.portal;

import com.github.kaysoro.kaellybot.core.commands.arguments.model.AbstractCommandArgument;
import com.github.kaysoro.kaellybot.core.commands.classic.PortalCommand;
import com.github.kaysoro.kaellybot.core.mapper.PortalMapper;
import com.github.kaysoro.kaellybot.core.model.constants.Constants;
import com.github.kaysoro.kaellybot.core.model.constants.Dimension;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import com.github.kaysoro.kaellybot.core.model.constants.Server;
import com.github.kaysoro.kaellybot.core.service.PortalService;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;

import java.util.regex.Matcher;

public class OnePortalArgument extends AbstractCommandArgument {

    private PortalService portalService;

    public OnePortalArgument(PortalCommand parent, PortalService portalService){
        super(parent, "\\s+(\\w+)\\s+(.+)", true);
        this.portalService = portalService;
    }

    @Override
    public void execute(Message message, Matcher matcher) {
        String dimension = matcher.group(1).trim();
        String server = matcher.group(2).trim();

        portalService.getPortal(Server.AGRIDE, Dimension.ECAFLIPUS, Constants.DEFAULT_LANGUAGE)
                .doOnSuccess(portal -> message.getChannel()
                        .flatMap(channel -> channel.createEmbed(spec -> PortalMapper.map(spec, portal)))
                        .subscribe()
                )
                .doOnError(error -> manageUnknownException(message, error))
                .subscribe();
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " dimension server` : "
                + Translator.getLabel(lg, "pos.one_portal");
    }
}