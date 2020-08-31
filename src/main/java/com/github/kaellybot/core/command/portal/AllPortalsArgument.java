package com.github.kaellybot.core.command.portal;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.Server;
import com.github.kaellybot.core.command.model.AbstractCommandArgument;
import com.github.kaellybot.core.command.model.Command;
import com.github.kaellybot.core.mapper.PortalMapper;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.service.PortalService;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.PermissionScope;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.regex.Matcher;

@Component
@Qualifier(PortalCommand.COMMAND_QUALIFIER)
public class AllPortalsArgument extends AbstractCommandArgument {

    private final PortalService portalService;
    private final PortalMapper portalMapper;

    public AllPortalsArgument(@Qualifier(PortalCommand.COMMAND_QUALIFIER) Command parent, PortalService portalService,
                              PortalMapper portalMapper, DiscordTranslator translator){
        super(parent, "\\s+(\\w+)", true, PermissionScope.EMBED_PERMISSIONS, translator);
        this.portalService = portalService;
        this.portalMapper = portalMapper;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
       // TODO determine server in the message
        Server server = Server.builder().labels(Map.of(language, "Mériana")).build();
        return portalService.getPortals(server, language)
                .flatMap(portal -> message.getChannel().flatMap(channel -> channel
                        .createEmbed(spec -> portalMapper.decorateSpec(spec, portal, language))));
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + " server` : "
                + translator.getLabel(lg, "pos.all_portals");
    }
}