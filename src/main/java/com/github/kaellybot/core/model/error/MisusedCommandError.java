package com.github.kaellybot.core.model.error;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.util.Translator;
import com.github.kaellybot.core.command.util.Command;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MisusedCommandError implements Error {

    private final Command command;

    @Override
    public String getLabel(Translator translator, Language language){
        return translator.getLabel(language, "error.misused_command", command.getName());
    }
}