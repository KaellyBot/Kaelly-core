package com.github.kaellybot.core.command.util;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.constant.Order;
import com.github.kaellybot.core.model.constant.Priority;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Flux;

public interface CommandArgument<T> {

    boolean triggerMessage(Message message, String prefix);

    boolean isArgumentHasPermissionsNeeded(PermissionSet permissions);

    boolean isUserHasPermissionsNeeded(PermissionSet permissions);

    boolean isChannelNSFWCompatible(MessageChannel channel);

    Flux<T> tryExecute(Message message, String prefix, Language language, PermissionSet botPermissions,
                       PermissionSet userPermissions);

    String help(Language lg, String prefix);

    boolean isDescribed();

    Priority getPriority();

    Order getOrder();
}