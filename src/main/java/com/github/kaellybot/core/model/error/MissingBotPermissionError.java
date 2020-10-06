package com.github.kaellybot.core.model.error;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.util.Translator;
import com.github.kaellybot.core.command.util.Command;
import discord4j.rest.util.Permission;
import lombok.AllArgsConstructor;

import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MissingBotPermissionError implements Error {

    private final Command command;
    private final Set<Permission> permissions;

    @Override
    public String getLabel(Translator translator, Language language){
        return permissions.stream()
                .map(permission -> translator.getLabel(language, "permission." + permission.name().toLowerCase()))
                .collect(Collectors.joining(", ", translator
                        .getLabel(language, "error.missing_bot_permission", command.getName()) + " ", "."));
    }
}
