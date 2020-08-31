package com.github.kaellybot.core.command.model;

import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.PermissionScope;
import org.apache.commons.lang.StringUtils;

public abstract class TextCommandArgument extends AbstractCommandArgument {

    public TextCommandArgument(Command parent, String subPattern, boolean isDescribed,
                               DiscordTranslator translator){
        super(parent, subPattern, isDescribed, PermissionScope.TEXT_PERMISSIONS, translator, Priority.NORMAL);
    }

    public TextCommandArgument(Command parent, DiscordTranslator translator){
        this(parent, StringUtils.EMPTY, false, translator);
    }

    public TextCommandArgument(Command parent, boolean isDescribed, DiscordTranslator translator){
        this(parent, StringUtils.EMPTY, isDescribed, translator);
    }
}
