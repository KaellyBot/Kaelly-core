package com.github.kaysoro.kaellybot.core.model.error;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.DiscordTranslator;

public class MissingNSFWOptionError implements Error {

    public String getLabel(DiscordTranslator translator, Language language){
        return translator.getLabel(language, "error.missing_nsfw");
    }
}
