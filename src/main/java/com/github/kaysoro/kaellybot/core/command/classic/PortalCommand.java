package com.github.kaysoro.kaellybot.core.command.classic;

import com.github.kaysoro.kaellybot.core.command.argument.portal.AllPortalsArgument;
import com.github.kaysoro.kaellybot.core.command.argument.portal.OnePortalArgument;
import com.github.kaysoro.kaellybot.core.command.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.service.PortalService;
import org.springframework.stereotype.Component;

@Component
public class PortalCommand extends AbstractCommand {

    public PortalCommand(PortalService portalService) {
        super("pos");

        getArguments().add(new OnePortalArgument(this, portalService));
        getArguments().add(new AllPortalsArgument(this, portalService));
    }
}