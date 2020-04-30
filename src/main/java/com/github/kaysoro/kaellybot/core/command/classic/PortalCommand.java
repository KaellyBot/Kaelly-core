package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.argument.portal.AllPortalsArgument;
import com.github.kaysoro.kaellybot.core.command.argument.portal.OnePortalArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.mapper.PortalMapper;
import com.github.kaysoro.kaellybot.core.service.PortalService;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.springframework.stereotype.Component;

@Component
public class PortalCommand extends AbstractCommand {

    public PortalCommand(PortalService portalService, PortalMapper portalMapper, Translator translator) {
        super("pos", translator);

        getArguments().add(new OnePortalArgument(this, portalService, portalMapper, translator));
        getArguments().add(new AllPortalsArgument(this, portalService, portalMapper, translator));
    }
}