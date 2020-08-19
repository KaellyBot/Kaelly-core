package com.github.kaysoro.kaellybot.core.command.model;

import com.github.kaysoro.kaellybot.core.util.Translator;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.channel.MessageChannel;
import discord4j.core.object.entity.channel.PrivateChannel;
import discord4j.core.object.entity.channel.TextChannel;
import discord4j.rest.util.PermissionSet;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    private boolean isNSFW;
    protected Translator translator;

    protected AbstractCommand(String name, List<CommandArgument<Message>> arguments, Translator translator){
        super();
        this.name = name;
        this.isPublic = true;
        this.isAdmin = false;
        this.isHidden = false;
        this.isNSFW = false;

        this.arguments = arguments;
        //this.arguments.add(new CommonHelpArgument(this, translator));
        this.translator = translator;
    }

    @Override
    public final Flux<?> request(Message message) {
        return Flux.fromIterable(arguments)
                .filter(argument -> argument.triggerMessage(message))
                .flatMap(argument -> getPermissions(message)
                        .flatMapMany(permissions -> argument.tryExecute(message, permissions)));
    }

    protected boolean isChannelAppropriateForNSFW(MessageChannel channel){
        return channel instanceof TextChannel && ((TextChannel) channel).isNsfw()
                || channel instanceof PrivateChannel;
    }

    private Mono<PermissionSet> getPermissions(Message message){
        return message.getChannel()
                .filter(channel -> channel instanceof TextChannel)
                .map(TextChannel.class::cast)
                .flatMap(channel -> channel.getEffectivePermissions(message.getClient().getSelfId()));
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
