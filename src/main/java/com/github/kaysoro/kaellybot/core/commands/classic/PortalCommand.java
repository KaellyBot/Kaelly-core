package com.github.kaysoro.kaellybot.core.commands.classic;

import com.github.kaysoro.kaellybot.core.commands.arguments.portal.OnePortalArgument;
import com.github.kaysoro.kaellybot.core.commands.model.AbstractCommand;
import com.github.kaysoro.kaellybot.core.service.PortalService;

public class PortalCommand extends AbstractCommand {

    public PortalCommand(PortalService portalService) {
        super("pos");

        getArguments().add(new OnePortalArgument(this, portalService));
    }
}