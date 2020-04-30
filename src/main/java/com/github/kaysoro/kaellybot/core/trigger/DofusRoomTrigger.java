package com.github.kaysoro.kaellybot.core.trigger;

import com.github.kaysoro.kaellybot.core.mapper.DofusRoomPreviewMapper;
import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.PreviewDto;
import com.github.kaysoro.kaellybot.core.service.DofusRoomService;
import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class DofusRoomTrigger implements Trigger {

    private final Pattern DOFUS_ROOM_REGEX = Pattern.compile(Constants.DOFUS_ROOM_BUILD_URL + "(\\d+)");

    private DofusRoomService dofusRoomService;

    public DofusRoomTrigger(DofusRoomService dofusRoomService){
        this.dofusRoomService = dofusRoomService;
    }

    @Override
    public boolean isTriggered(Message message) {
        return DOFUS_ROOM_REGEX.matcher(message.getContent()).find();
    }

    @Override
    public void execute(Message message) {
        Matcher m = DOFUS_ROOM_REGEX.matcher(message.getContent());
        Set<String> ids = new HashSet<>();
        while(m.find()) ids.add(m.group(1));

        Flux<PreviewDto> previews = Flux.fromIterable(ids)
                .flatMap(id -> dofusRoomService.getDofusRoomPreview(id, Constants.DEFAULT_LANGUAGE));

        Flux.zip(previews, message.getAuthorAsMember(), message.getChannel())
                .flatMap(tuple -> tuple.getT3().createEmbed(spec -> DofusRoomPreviewMapper
                        .decorateSpec(spec, tuple.getT1(), tuple.getT2(), Constants.DEFAULT_LANGUAGE)))
                .subscribe();
    }
}
