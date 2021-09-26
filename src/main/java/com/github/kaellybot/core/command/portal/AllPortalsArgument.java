package com.github.kaellybot.core.command.portal;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.mapper.PortalMapper;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.service.PortalService;
import com.github.kaellybot.core.util.annotation.BotPermissions;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.function.Predicate;
import java.util.regex.Matcher;

import static com.github.kaellybot.core.model.constant.PermissionScope.EMBED_PERMISSIONS;

@Component
@Qualifier(PortalCommand.COMMAND_QUALIFIER)
@BotPermissions(EMBED_PERMISSIONS)
@Described
public class AllPortalsArgument extends AbstractCommandArgument {

    private final PortalService portalService;
    private final PortalMapper portalMapper;

    public AllPortalsArgument(@Qualifier(PortalCommand.COMMAND_QUALIFIER) Command parent, PortalService portalService,
                              PortalMapper portalMapper, DiscordTranslator translator){
        super(parent, translator);
        this.portalService = portalService;
        this.portalMapper = portalMapper;
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
        return translator.getServer(interaction)
                .filter(Predicate.not(Constants.UNKNOWN_SERVER::equals))
                .flatMapMany(server -> portalService.getPortals(server, language))
                .flatMap(portal -> interaction.getChannel().flatMap(channel -> channel
                        .createEmbed(spec -> portalMapper.decorateSpec(spec, portal, language))))
                .switchIfEmpty(interaction.getChannel().flatMap(channel -> channel
                        .createMessage(translator.getLabel(language, "pos.default_server_not_found"))));
    }

    @Override
    public String help(Language lg){
        return "`" + getParent().getName() + "` : " + translator.getLabel(lg, "pos.all_portals");
    }
}