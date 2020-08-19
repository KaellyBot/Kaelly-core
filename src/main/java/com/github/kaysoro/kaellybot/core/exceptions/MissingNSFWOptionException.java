package com.github.kaysoro.kaellybot.core.exceptions;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;

public class MissingNSFWOptionException implements Exception {

    public String getLabel(Translator translator, Language language){
        return translator.getLabel(language, "exception.missing_nsfw");
    }
}
