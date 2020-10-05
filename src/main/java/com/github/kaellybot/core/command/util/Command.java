package com.github.kaellybot.core.command.util;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Flux;

public interface Command {

    String getName();

    Flux<Message> request(Message message, String prefix, Language language);

    Flux<Message> sendException(Message message, Language language, PermissionSet permissions, Error error);

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
     * @param prefix Prefix for command
     * @return Short description of the command
     */
    String help(Language lg, String prefix);

    /**
     * @param prefix Prefix for command
     * @return Detailed description of the command
     */
    String moreHelp(Language lg, String prefix);
}
