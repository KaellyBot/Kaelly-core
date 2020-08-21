package com.github.kaysoro.kaellybot.core.model.error;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import discord4j.rest.util.Permission;

import java.util.Set;

public final class ErrorFactory {

    private ErrorFactory(){}

    public static Error createMissingPermissionException(Command command, Set<Permission> permissions){
        return new MissingPermissionError(command, permissions);
    }

    public static Error createMissingNSFWOptionException(){
        return new MissingNSFWOptionError();
    }
}
