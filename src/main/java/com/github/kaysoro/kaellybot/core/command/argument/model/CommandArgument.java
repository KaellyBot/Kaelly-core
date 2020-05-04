package com.github.kaysoro.kaellybot.core.command.argument.model;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Flux;

public interface CommandArgument<T> {

    boolean triggerMessage(Message message);

    boolean isArgumentHasPermissionsNeeded(PermissionSet permissions);

    Flux<T> execute(Message message);

    String help(Language lg, String prefix);

    boolean isDescribed();
}