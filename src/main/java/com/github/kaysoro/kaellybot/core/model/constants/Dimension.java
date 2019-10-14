package com.github.kaysoro.kaellybot.core.model.constants;

import com.github.kaysoro.kaellybot.core.util.Translator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@Getter
@AllArgsConstructor
public enum Dimension {

    ENUTROSOR("dimension.enutrosor", "http://image.noelshack.com/fichiers/2019/02/2/1546904136-enutrosor.png", new Color(255, 255, 104)),
    SRAMBAD  ("dimension.srambad"  , "http://image.noelshack.com/fichiers/2019/02/2/1546904136-srambad.png", new Color(47, 66, 104)),
    XELORIUM ("dimension.xelorium" , "http://image.noelshack.com/fichiers/2019/02/2/1546904136-xelorium.png", new Color(110, 81, 105)),
    ECAFLIPUS("dimension.ecaflipus", "http://image.noelshack.com/fichiers/2019/02/2/1546904136-ecaflipus.png", new Color(205, 216, 158));

    private String key;

    private String image;

    private Color color;

    public String getLabel(Language lang){
        return Translator.getLabel(lang, getKey());
    }
}