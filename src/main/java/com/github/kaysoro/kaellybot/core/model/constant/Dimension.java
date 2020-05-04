package com.github.kaysoro.kaellybot.core.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.*;

@Getter
@AllArgsConstructor
public enum Dimension implements MultilingualEnum {

    ENUTROSOR("dimension.enutrosor", "https://i.imgur.com/ssMAcx3.png", new Color(255, 255, 104)),
    SRAMBAD  ("dimension.srambad"  , "https://i.imgur.com/jzpizTm.png", new Color(47, 66, 104)),
    XELORIUM ("dimension.xelorium" , "https://i.imgur.com/vfQhS5D.png", new Color(110, 81, 105)),
    ECAFLIPUS("dimension.ecaflipus", "https://i.imgur.com/sLK4FmQ.png", new Color(205, 216, 158));

    private String key;

    private String image;

    private Color color;
}