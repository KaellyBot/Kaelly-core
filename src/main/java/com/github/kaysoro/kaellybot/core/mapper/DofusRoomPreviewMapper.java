package com.github.kaysoro.kaellybot.core.mapper;

import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.DofusRoomPreviewDto;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.EmbedCreateSpec;

public final class DofusRoomPreviewMapper {

    private DofusRoomPreviewMapper(){}

    public static void decorateSpec(EmbedCreateSpec spec, DofusRoomPreviewDto preview, Member author, Language language){
        spec.setTitle("test");
        //TODO
    }
}