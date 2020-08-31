package com.github.kaellybot.core.model.error;

import com.github.kaellybot.commons.model.constants.Error;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.util.Translator;

public class MissingNSFWOptionError implements Error {

    @Override
    public String getLabel(Translator translator, Language language) {
        return translator.getLabel(language, "error.missing_nsfw");
    }
}
