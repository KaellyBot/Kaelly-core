package com.github.kaysoro.kaellybot.core.model.constant;

import java.awt.*;
import java.util.List;

public final class Constants {

    private Constants(){}

    public static final String DEFAULT_PREFIX = "!!";

    public static final Language DEFAULT_LANGUAGE = Language.FR;

    public static final String NAME = "Kaelly";

    public static final String VERSION = "2.0.0";

    public static final String CHANGELOG = "https://i.imgur.com/0qQSOac.png";

    public static final Color COLOR = new Color(127, 76, 138);

    public static final Game GAME = Game.DOFUS;

    public static final long AUTHOR_ID = 162842827183751169L;

    public static final String AVATAR = "https://avatars3.githubusercontent.com/u/44924023?s=400&u=7bc255b104084f0762db20e6aba4e9ff549de467&v=4";

    public static final String TWITTER = "https://twitter.com/KaellyBot";
    
    public static final  String GIT = "https://github.com/Kaysoro/KaellyBot";

    public static final String INVITE = "https://discordapp.com/oauth2/authorize?&client_id=202916641414184960&scope=bot";

    public static final String PAYPAL = "https://paypal.me/kaysoro";

    public static final String DISCORD_INVITE = "https://discord.gg/VsrbrYC";

    public static final List<String> DOFUS_ROOM_BUILD_URL = List.of(
            "https://www.dofusroom.com/buildroom/build/show/",
            "https://dofusroom.com/b-");
}