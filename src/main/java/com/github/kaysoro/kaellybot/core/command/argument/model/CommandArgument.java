package com.github.kaysoro.kaellybot.core.command.argument.model;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.PermissionSet;

public interface CommandArgument {

    boolean triggerMessage(Message message);

    boolean isArgumentHasPermissionsNeeded(PermissionSet permissions);

    void execute(Message message);

    String help(Language lg, String prefix);

    boolean isDescribed();
}
