package com.github.kaellybot.core.model.constant;

import com.github.kaellybot.commons.model.constants.Game;
import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.commons.model.entity.Server;

import java.util.List;

public final class Constants {

    private Constants(){}

    public static final String DEFAULT_PREFIX = "!!";

    public static final Language DEFAULT_LANGUAGE = Language.FR;

    public static final Server UNKNOWN_SERVER = Server.builder()
            .id("UNKNOWN_SERVER")
            .imgUrl("https://i.imgur.com/xz8jzK0.png")
            .build();

    public static final String NAME = "Kaelly";

    public static final String VERSION = "2.0.0";

    public static final int COLOR = 8342666;

    public static final Game GAME = Game.DOFUS;

    public static final String CHANGELOG = "https://i.imgur.com/0qQSOac.png";

    public static final String AVATAR = "https://avatars3.githubusercontent.com/u/44924023?s=400&u=7bc255b104084f0762db20e6aba4e9ff549de467&v=4";

    public static final String TWITTER = "https://twitter.com/KaellyBot";
    
    public static final  String GIT = "https://github.com/Kaysoro/KaellyBot";

    public static final String INVITE = "https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot";

    public static final String PAYPAL = "https://paypal.me/kaysoro";

    public static final String DISCORD_INVITE = "https://discord.gg/VsrbrYC";

    public static final List<String> DOFUS_ROOM_BUILD_URL = List.of(
            "https://www.dofusroom.com/buildroom/build/show/",
            "https://dofusroom.com/b-");

    public static final String DOFUS_ROOM_BUILD_URL_REFERRER = "https://www.dofusroom.com/buildroom/build/show/{}?referrer=kaellybot";
}