package com.github.kaysoro.kaellybot.core.model.error;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MisusedCommandError implements Error {

    private final String prefix;
    private final Command command;

    public String getLabel(Translator translator, Language language){
        return translator.getLabel(language, "error.misused_command", prefix + command.getName());
    }
}
