package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import org.apache.commons.lang.StringUtils;

public abstract class TextCommandArgument extends AbstractCommandArgument {

    public TextCommandArgument(Command parent, String subPattern, boolean isDescribed,
                                Translator translator){
        super(parent, subPattern, isDescribed, PermissionScope.TEXT_PERMISSIONS, translator, Priority.NORMAL);
    }

    public TextCommandArgument(Command parent, Translator translator){
        this(parent, StringUtils.EMPTY, false, translator);
    }

    public TextCommandArgument(Command parent, boolean isDescribed, Translator translator){
        this(parent, StringUtils.EMPTY, isDescribed, translator);
    }
}
