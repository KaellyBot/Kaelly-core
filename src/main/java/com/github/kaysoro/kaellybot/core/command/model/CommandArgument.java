package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import discord4j.core.object.entity.Message;
import discord4j.rest.util.PermissionSet;
import reactor.core.publisher.Flux;

public interface CommandArgument<T> extends Comparable<CommandArgument<T>> {

    boolean triggerMessage(Message message);

    boolean isArgumentHasPermissionsNeeded(PermissionSet permissions);

    Flux<T> tryExecute(Message message, PermissionSet permissions);

    String help(Language lg, String prefix);

    boolean isDescribed();

    Priority getPriority();

    enum Priority {
        IMMEDIATE, HIGH, MEDIUM, NORMAL, LOW
    }
}