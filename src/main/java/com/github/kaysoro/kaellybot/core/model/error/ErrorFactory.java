package com.github.kaysoro.kaellybot.core.model.error;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import discord4j.rest.util.Permission;

import java.util.Set;

public final class ErrorFactory {

    private ErrorFactory(){}

    public static Error createMissingPermissionError(Command command, Set<Permission> permissions){
        return new MissingPermissionError(command, permissions);
    }

    public static Error createMissingNSFWOptionError(){
        return new MissingNSFWOptionError();
    }

    public static Error createUnknownError(){
        return new UnknownError();
    }
}
