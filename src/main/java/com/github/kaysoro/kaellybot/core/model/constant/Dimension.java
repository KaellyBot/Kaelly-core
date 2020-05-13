package com.github.kaysoro.kaellybot.core.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Dimension implements MultilingualEnum {

    ENUTROSOR("dimension.enutrosor", "https://i.imgur.com/ssMAcx3.png", 16777064),
    SRAMBAD  ("dimension.srambad"  , "https://i.imgur.com/jzpizTm.png", 3097192 ),
    XELORIUM ("dimension.xelorium" , "https://i.imgur.com/vfQhS5D.png", 7229801 ),
    ECAFLIPUS("dimension.ecaflipus", "https://i.imgur.com/sLK4FmQ.png", 13490334);

    private final String key;

    private final String image;

    private final int color;
}