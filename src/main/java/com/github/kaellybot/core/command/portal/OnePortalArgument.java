package com.github.kaellybot.core.command.portal;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.Dimension;
import com.github.kaellybot.commons.model.entity.Server;
import com.github.kaellybot.commons.service.DimensionService;
import com.github.kaellybot.core.command.util.Command;
import com.github.kaellybot.core.command.util.AbstractCommandArgument;
import com.github.kaellybot.core.mapper.PortalMapper;
import com.github.kaellybot.core.model.constant.Order;
import com.github.kaellybot.core.service.PortalService;
import com.github.kaellybot.core.util.annotation.BotPermissions;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.annotation.Described;
import com.github.kaellybot.core.util.annotation.DisplayOrder;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.regex.Matcher;

import static com.github.kaellybot.core.model.constant.PermissionScope.EMBED_PERMISSIONS;


@Component
@Qualifier(PortalCommand.COMMAND_QUALIFIER)
@BotPermissions(EMBED_PERMISSIONS)
@DisplayOrder(Order.SECOND)
@Described
public class OnePortalArgument extends AbstractCommandArgument {

    private final PortalService portalService;
    private final PortalMapper portalMapper;
    private final DimensionService dimensionService;

    public OnePortalArgument(@Qualifier(PortalCommand.COMMAND_QUALIFIER) Command parent, PortalService portalService,
                             DimensionService dimensionService, PortalMapper portalMapper, DiscordTranslator translator){
        super(parent, "\\s+(\\w+)", translator);
        this.portalService = portalService;
        this.portalMapper = portalMapper;
        this.dimensionService= dimensionService;
    }

    @Override
    public Flux<Message> execute(Interaction interaction, Language language, Matcher matcher) {
       // TODO determine server & dimension in the message
        Server server = Server.builder().labels(Map.of(language, "Mériana")).build();
        Dimension dimension = Dimension.builder().labels(Map.of(language, "Enutrosor")).build();

        return portalService.getPortal(server, dimension, language)
                .flatMap(portal -> interaction.getChannel().flatMap(channel -> channel
                        .createEmbed(spec -> portalMapper.decorateSpec(spec, portal, language))))
                .flatMapMany(Flux::just);
    }

    @Override
    public String help(Language lg){
        return "`" + getParent().getName() + " Srambad` : " + translator.getLabel(lg, "pos.one_portal");
    }
}