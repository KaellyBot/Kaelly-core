package com.github.kaysoro.kaellybot.core.model.error;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;

public class MissingNSFWOptionError implements Error {

    public String getLabel(Translator translator, Language language){
        return translator.getLabel(language, "exception.missing_nsfw");
    }
}
