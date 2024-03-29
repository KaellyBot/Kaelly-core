package com.github.kaellybot.core.model.error;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.core.command.util.Command;
import discord4j.rest.util.Permission;

import java.util.Set;

public final class ErrorFactory {

    private ErrorFactory(){}

    public static Error createMisusedCommandError(Command command){
        return new MisusedCommandError(command);
    }

    public static Error createMissingNSFWOptionError(){
        return new MissingNSFWOptionError();
    }

    public static Error createMissingBotPermissionError(Command command, Set<Permission> permissions){
        return new MissingBotPermissionError(command, permissions);
    }

    public static Error createMissingUserPermissionError(Command command, Set<Permission> permissions){
        return new MissingUserPermissionError(command, permissions);
    }

    public static Error createUnknownError(){
        return new UnknownError();
    }
}
