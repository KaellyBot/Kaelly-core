package com.github.kaellybot.core.command.model;

import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.PermissionScope;
import org.apache.commons.lang.StringUtils;

public abstract class EmbedCommandArgument extends AbstractCommandArgument {

    public EmbedCommandArgument(Command parent, String subPattern, boolean isDescribed,
                                DiscordTranslator translator){
        super(parent, subPattern, isDescribed, PermissionScope.EMBED_PERMISSIONS, translator, Priority.NORMAL);
    }

    public EmbedCommandArgument(Command parent, boolean isDescribed, DiscordTranslator translator){
        this(parent, StringUtils.EMPTY, isDescribed, translator);
    }

    public EmbedCommandArgument(Command parent, DiscordTranslator translator){
        this(parent, StringUtils.EMPTY, false, translator);
    }
}