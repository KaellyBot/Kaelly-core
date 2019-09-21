package com.github.kaysoro.kaellybot.core.model.constants;

import lombok.Getter;

@Getter
public enum Game {

    DOFUS("Dofus", Constants.INVITE),
    DOFUS_TOUCH("Dofus Touch", "https://discordapp.com/oauth2/authorize?&client_id=393925392618094612&scope=bot");

    private String name;
    private String botInvite;

    Game(String name, String botInvite){
        this.name = name;
        this.botInvite = botInvite;
    }
}