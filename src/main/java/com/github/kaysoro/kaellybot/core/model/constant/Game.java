package com.github.kaysoro.kaellybot.core.model.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Game {

    DOFUS("Dofus", Constants.INVITE),
    DOFUS_TOUCH("Dofus Touch", "https://discordapp.com/oauth2/authorize?&client_id=393925392618094612&scope=bot");

    private String name;
    private String botInvite;
}