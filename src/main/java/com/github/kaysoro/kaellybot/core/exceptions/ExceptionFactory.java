package com.github.kaysoro.kaellybot.core.exceptions;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import discord4j.rest.util.Permission;

import java.util.Set;

public final class ExceptionFactory {

    public static Exception createMissingPermissionException(Command command, Set<Permission> permissions){
        return new MissingPermissionException(command, permissions);
    }

    public static Exception createMissingNSFWOptionException(){
        return new MissingNSFWOptionException();
    }
}
