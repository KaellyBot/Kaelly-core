package com.github.kaysoro.kaellybot.core.model.constants;

import com.github.kaysoro.kaellybot.core.util.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Dimension {

    ENUTROSOR("dimension.enutrosor"),
    SRAMBAD  ("dimension.srambad"  ),
    XELORIUM ("dimension.xelorium" ),
    ECAFLIPUS("dimension.ecaflipus");

    private String key;

    public String getLabel(Language lang){
        return Translator.getLabel(lang, getKey());
    }
}