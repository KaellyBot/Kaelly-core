package com.github.kaellybot.core.trigger;

import com.github.kaellybot.core.command.help.HelpCommand;
import com.github.kaellybot.core.command.help.HelpNoArgument;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class MentionTrigger extends AbstractTrigger {

    private final HelpNoArgument helpNoArgument;

    public MentionTrigger(DiscordTranslator translator, HelpNoArgument helpNoArgument){
        super(translator);
        this.helpNoArgument = helpNoArgument;
    }

    @Override
    protected boolean isPatternFound(Message message){
        return message.getUserMentionIds().contains(message.getClient().getSelfId())
                && message.getContent().matches("<@!?" + message.getClient().getSelfId().asString() + ">");
    }

    @Override
    public Flux<Message> execute(Message message) {
        return translator.getLanguage(message)
                .flatMap(language -> message.getChannel()
                        .flatMap(channel -> channel.createMessage(translator.getRandomLabel(language,
                                "mention.help", HelpCommand.COMMAND_NAME,
                                helpNoArgument.getCommandList(language)))))
                .flatMapMany(Flux::just);
    }
}