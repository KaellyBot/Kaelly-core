package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.model.error.Error;
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
     * Is the command available by the admin ?
     * @return true is the command is available, else false.
     */
    boolean isPublic();

    /**
     * is the command is hidden ?
     * @return True if the command is hidden
     */
    boolean isHidden();

    /**
     * Change the command scope
     * @param isPublic is command available or not
     */
    void setPublic(boolean isPublic);

    /**
     * Change the command scope for admin user
     * @param isAdmin is command only available for admins or not
     */
    void setAdmin(boolean isAdmin);

    /**
     * Hide or not the command
     * @param isHidden is command hidden or not
     */
    void setHidden(boolean isHidden);

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
