package com.github.kaellybot.core.command.portal;

import com.github.kaellybot.commons.model.constants.Language;
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

import java.util.function.Predicate;
import java.util.regex.Matcher;

@Component
@Qualifier(PortalCommand.COMMAND_QUALIFIER)
public class AllPortalsArgument extends AbstractCommandArgument {

    private final PortalService portalService;
    private final PortalMapper portalMapper;

    public AllPortalsArgument(@Qualifier(PortalCommand.COMMAND_QUALIFIER) Command parent, PortalService portalService,
                              PortalMapper portalMapper, DiscordTranslator translator){
        super(parent,true, PermissionScope.EMBED_PERMISSIONS, translator);
        this.portalService = portalService;
        this.portalMapper = portalMapper;
    }

    @Override
    public Flux<Message> execute(Message message, String prefix, Language language, Matcher matcher) {
        return translator.getServer(message)
                .filter(Predicate.not(Constants.UNKNOWN_SERVER::equals))
                .flatMapMany(server -> portalService.getPortals(server, language))
                .flatMap(portal -> message.getChannel().flatMap(channel -> channel
                        .createEmbed(spec -> portalMapper.decorateSpec(spec, portal, language))))
                .switchIfEmpty(message.getChannel().flatMap(channel -> channel
                        .createMessage(translator.getLabel(language, "pos.default_server_not_found"))));
    }

    @Override
    public String help(Language lg, String prefix){
        return prefix + "`" + getParent().getName() + "` : " + translator.getLabel(lg, "pos.all_portals");
    }
}