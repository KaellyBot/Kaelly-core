package com.github.kaysoro.kaellybot.core.command.argument.model;

import com.github.kaysoro.kaellybot.core.command.model.Command;
import com.github.kaysoro.kaellybot.core.util.PermissionScope;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Message;
import java.util.function.Consumer;

public class TextCommandArgument extends BasicCommandArgument {

    public TextCommandArgument(Command parent, Translator translator, Consumer<Message> executor) {
        super(parent, translator, PermissionScope.TEXT_PERMISSIONS, executor);
    }
}
