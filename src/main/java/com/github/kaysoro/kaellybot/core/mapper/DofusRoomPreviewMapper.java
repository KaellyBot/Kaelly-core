package com.github.kaysoro.kaellybot.core.mapper;

import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.PreviewDto;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.object.entity.Member;
import discord4j.core.spec.MessageCreateSpec;
import org.springframework.stereotype.Component;

@Component
public class DofusRoomPreviewMapper {

    private static final String ATTACHEMENT_FILENAME = "attachment://dofusroompreview.png";
    private Translator translator;

    public DofusRoomPreviewMapper(Translator translator){
        this.translator = translator;
    }

    public void decorateSpec(MessageCreateSpec spec, PreviewDto preview, Member author, Language language){
        spec.setEmbed(embedSpec -> embedSpec.setTitle(preview.getData().getName())
                .setDescription(translator.getLabel(language, "dofusroom.made_by", preview.getData().getAuthor()))
                .setThumbnail("https://i.imgur.com/qLh0qRM.png")
                .setUrl(Constants.DOFUS_ROOM_BUILD_URL + preview.getId())
                .setImage(ATTACHEMENT_FILENAME)
                .setFooter(translator.getLabel(language, "dofusroom.shared_by", author.getDisplayName()),
                        author.getAvatarUrl()));
        //TODO
    }
}