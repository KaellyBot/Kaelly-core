package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.command.argument.common.HelpArgument;
import com.github.kaysoro.kaellybot.core.command.argument.model.CommandArgument;
import com.github.kaysoro.kaellybot.core.util.Translator;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.PermissionSet;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class AbstractCommand implements Command {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCommand.class);

    protected String name;
    protected List<CommandArgument<Message>> arguments;
    private boolean isPublic;
    private boolean isAdmin;
    private boolean isHidden;
    protected Translator translator;

    protected AbstractCommand(String name, Translator translator){
        super();
        this.name = name;
        this.isPublic = true;
        this.isAdmin = false;
        this.isHidden = false;

        this.arguments = new ArrayList<>();
        this.arguments.add(new HelpArgument(this, translator));
        this.translator = translator;
    }

    @Override
    public final Flux<?> request(Message message) {
        return Flux.fromIterable(arguments)
                .filter(argument -> argument.triggerMessage(message))
                .flatMap(argument -> getPermissions(message)
                        .flatMapMany(permissions -> argument.tryExecute(message, permissions)));
    }

    private Mono<PermissionSet> getPermissions(Message message){
        return message.getChannel()
                .filter(channel -> channel instanceof TextChannel)
                .map(TextChannel.class::cast)
                .zipWith(message.getClient().getSelfId())
                .flatMap(tuple -> tuple.getT1().getEffectivePermissions(tuple.getT2()));
    }

    @Override
    public String help(Language lg, String prefix){
        return "**" + prefix + name + "** " + translator.getLabel(lg, name.toLowerCase() + ".help");
    }

    @Override
    public String moreHelp(Language lg, String prefix){
        return help(lg, prefix) + arguments.stream().filter(CommandArgument::isDescribed)
                .map(arg -> "\n" + arg.help(lg, prefix))
                .reduce(StringUtils.EMPTY, (arg1, arg2) -> arg1 + arg2);
    }
}
