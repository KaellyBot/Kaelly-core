package com.github.kaellybot.core.model.error;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.core.command.model.Command;
import discord4j.rest.util.Permission;

import java.util.Set;

public final class ErrorFactory {

    private ErrorFactory(){}

    public static Error createMisusedCommandError(String prefix, Command command){
        return new MisusedCommandError(prefix, command);
    }

    public static Error createMissingNSFWOptionError(){
        return new MissingNSFWOptionError();
    }

    public static Error createMissingPermissionError(Command command, Set<Permission> permissions){
        return new MissingPermissionError(command, permissions);
    }

    public static Error createUnknownError(){
        return new UnknownError();
    }
}
