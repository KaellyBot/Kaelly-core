package com.github.kaellybot.core.command.util;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import discord4j.core.event.domain.interaction.ApplicationCommandInteractionEvent;
import discord4j.core.object.command.Interaction;
import discord4j.core.object.entity.Message;
import discord4j.discordjson.json.ApplicationCommandRequest;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Flux;

public interface Command {

    Flux<Void> request(ApplicationCommandInteractionEvent event, Language language);

    Flux<Message> sendException(Interaction interaction, Language language, PermissionSet permissions, Error error);

    String getName();

    ApplicationCommandRequest getApplicationCommandRequest();

    /**
     * Is the command only usable by admins ?
     * @return True if it only can be used by admin, else false.
     */
    boolean isAdmin();

    /**
     * is the command is hidden ?
     * @return True if the command is hidden
     */
    boolean isHidden();

    /**
     * @return Short description of the command
     */
    String help(Language lg);

    /**
     * @return Detailed description of the command
     */
    String moreHelp(Language lg);
}
