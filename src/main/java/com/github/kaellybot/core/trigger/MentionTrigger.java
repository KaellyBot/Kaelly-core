package com.github.kaellybot.core.trigger;

import com.github.kaellybot.core.command.help.HelpCommand;
import com.github.kaellybot.core.command.help.HelpNoArgument;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.util.PermissionScope;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MentionTrigger extends AbstractTrigger {

    private final HelpNoArgument helpNoArgument;

    public MentionTrigger(DiscordTranslator translator, HelpNoArgument helpNoArgument){
        super(translator, PermissionScope.TEXT_PERMISSIONS);
        this.helpNoArgument = helpNoArgument;
    }

    @Override
    protected boolean isPatternFound(Message message){
        return message.getUserMentionIds().contains(message.getClient().getSelfId())
                && message.getContent().matches("<@!?" + message.getClient().getSelfId().asString() + ">");
    }

    @Override
    public Flux<Message> execute(Message message) {
        return translator.getPrefix(message)
                .zipWith(translator.getLanguage(message))
                .flatMap(tuple -> message.getChannel()
                        .flatMap(channel -> channel.createMessage(translator.getRandomLabel(tuple.getT2(),
                                "mention.help", tuple.getT1() + HelpCommand.COMMAND_NAME,
                                helpNoArgument.getCommandList(tuple.getT2(), tuple.getT1())))))
                .flatMapMany(Flux::just);
    }
}