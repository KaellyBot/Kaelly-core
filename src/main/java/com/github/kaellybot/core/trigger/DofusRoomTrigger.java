package com.github.kaellybot.core.trigger;

import com.github.kaellybot.core.mapper.DofusRoomPreviewMapper;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.payload.dofusroom.StatusDto;
import com.github.kaellybot.core.service.DofusRoomService;
import com.github.kaellybot.core.util.annotation.BotPermissions;
import com.github.kaellybot.core.util.DiscordTranslator;
import com.github.kaellybot.core.model.constant.PermissionScope;
import discord4j.core.object.entity.Message;
import lombok.Builder;
import lombok.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@BotPermissions(PermissionScope.EMBED_PERMISSIONS)
public class DofusRoomTrigger extends AbstractTrigger {

    private final List<Pattern> dofusRoomUrlPatterns;

    private final DofusRoomService dofusRoomService;

    private final DofusRoomPreviewMapper dofusRoomPreviewMapper;

    public DofusRoomTrigger(DiscordTranslator translator, DofusRoomService dofusRoomService,
                            DofusRoomPreviewMapper dofusRoomPreviewMapper){
        super(translator);
        this.dofusRoomService = dofusRoomService;
        this.dofusRoomPreviewMapper = dofusRoomPreviewMapper;
        this.dofusRoomUrlPatterns = Constants.DOFUS_ROOM_BUILD_URL.parallelStream()
                .map(Pattern::compile)
                .collect(Collectors.toList());
    }

    @Override
    protected boolean isPatternFound(Message message){
        return dofusRoomUrlPatterns.parallelStream()
                .map(pattern -> pattern.matcher(message.getContent()).find())
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    @Override
    public Flux<Message> execute(Message message) {
        return Flux.fromStream(dofusRoomUrlPatterns.parallelStream()
                .map(pattern -> pattern.matcher(message.getContent()))
                .flatMap(this::findAllDofusRoomIds)
                .distinct()).collectList()
                .zipWith(translator.getLanguage(message))
                .flatMapMany(tuple -> Flux.fromIterable(tuple.getT1())
                        .flatMap(id -> dofusRoomService.getDofusRoomPreview(id.getId(), id.getToken(), tuple.getT2()))
                        .filter(preview -> StatusDto.SUCCESS.equals(preview.getStatus()))
                        .collectList()
                        .zipWith(message.getChannel())
                        .flatMapMany(tuple2 -> Flux.fromIterable(tuple2.getT1())
                                .flatMap(preview -> tuple2.getT2().createMessage(spec -> dofusRoomPreviewMapper
                                        .decorateSpec(spec, preview, tuple.getT2())))))
                .onErrorResume(error -> manageUnknownException(message, error));
    }

    private Stream<DofusRoomId> findAllDofusRoomIds(Matcher m){
        List<DofusRoomId> ids = new ArrayList<>();
        while(m.find()) {
            if (m.groupCount() == 2)
                ids.add(DofusRoomId.builder().id(m.group(2)).token(m.group(1)).build());
            else
                ids.add(DofusRoomId.builder().id(m.group(1)).build());
        }
        return ids.parallelStream();
    }

    @Value
    @Builder
    private static class DofusRoomId {
        String id;
        String token;
    }
}