package com.github.kaysoro.kaellybot.core.exceptions;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.util.Translator;

public interface Exception {

    String getLabel(Translator translator, Language language);
}
