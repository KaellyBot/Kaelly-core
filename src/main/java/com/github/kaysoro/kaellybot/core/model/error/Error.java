package com.github.kaysoro.kaellybot.core.model.error;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.DiscordTranslator;

public interface Error {

    String getLabel(DiscordTranslator translator, Language language);
}
