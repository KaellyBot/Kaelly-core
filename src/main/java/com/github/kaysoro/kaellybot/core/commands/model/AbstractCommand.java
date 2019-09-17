package com.github.kaysoro.kaellybot.core.commands.model;

import com.github.kaysoro.kaellybot.core.commands.util.Translator;
import com.github.kaysoro.kaellybot.core.model.constants.DiscordConstants;
import com.github.kaysoro.kaellybot.core.model.constants.Language;
import discord4j.core.object.entity.Message;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractCommand implements Command {

    private final static Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);
    private final String VOID_MESSAGE = "";

    protected String name;
    protected List<CommandArgument> arguments;
    private boolean isPublic;
    private boolean isUsableInMP;
    private boolean isAdmin;
    private boolean isHidden;


    protected AbstractCommand(String name){
        super();
        this.name = name;
        this.isPublic = true;
        this.isUsableInMP = true;
        this.isAdmin = false;
        this.isHidden = false;

        this.arguments = new ArrayList<>();
        this.arguments.add(new AbstractCommandArgument(this, "\\s+help") {
            @Override
            public void execute(Message message) {
                message.getChannel().flatMap(channel -> channel
                        .createMessage(moreHelp(Language.FR, DiscordConstants.DEFAULT_PREFIX)))
                        .subscribe();
            }

            @Override
            public String help(Language lg, String prefix){
                return prefix + "`" + getParent().getName() + " help` : " + Translator.getLabel(lg, "help.help");
            }
        });
    }

    @Override
    public final void request(Message message) {
        arguments.stream()
                .filter(arg -> arg.triggerMessage(message))
                .forEach(arg -> arg.execute(message));
    }

    @Override
    public String help(Language lg, String prefix){
        return "**" + prefix + name + "** " + Translator.getLabel(lg, name.toLowerCase() + ".help");
    }

    @Override
    public String moreHelp(Language lg, String prefix){
        return help(lg, prefix) + arguments.stream().filter(CommandArgument::isDescribed)
                .map(arg -> "\n" + arg.help(lg, prefix))
                .reduce(VOID_MESSAGE, (arg1, arg2) -> arg1 + arg2);
    }
}
