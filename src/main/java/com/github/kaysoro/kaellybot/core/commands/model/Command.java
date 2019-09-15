package com.github.kaysoro.kaellybot.core.commands.model;

import com.github.kaysoro.kaellybot.core.model.constants.Language;
import discord4j.core.object.entity.Message;

public interface Command {
    String getName();

    void request(Message message);

    /**
     * Is the command usable in MP?
     * @return True if it can be used in MP, else false.
     */
    boolean isUsableInMP();

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
     * Change the command scope in MP
     * @param isUsableInMP is command available in MP or not
     */
    void setUsableInMP(boolean isUsableInMP);

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
