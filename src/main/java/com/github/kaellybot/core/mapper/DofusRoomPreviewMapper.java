package com.github.kaellybot.core.mapper;

import com.github.kaellybot.commons.model.constants.Language;
import com.github.kaellybot.core.model.constant.Constants;
import com.github.kaellybot.core.payload.dofusroom.PreviewDto;
import com.github.kaellybot.core.payload.dofusroom.RingDto;
import com.github.kaellybot.core.payload.dofusroom.StuffDto;
import com.github.kaellybot.core.payload.dofusroom.TrophusDto;
import com.github.kaellybot.core.util.DofusRoomPreviewProcessor;
import com.github.kaellybot.core.util.DiscordTranslator;
import discord4j.core.spec.MessageCreateSpec;
import static com.github.kaellybot.core.util.DofusRoomPreviewProcessor.ItemPosition;

import discord4j.core.spec.legacy.LegacyMessageCreateSpec;
import discord4j.rest.util.Color;
import org.openimaj.image.MBFImage;
import org.springframework.stereotype.Component;
import java.io.*;
import java.util.Optional;

@Component
public class DofusRoomPreviewMapper {

    private static final String ATTACHMENT_FILENAME = Constants.NAME + "_" + "Dofusroom_Preview.png";
    private final DiscordTranslator translator;
    private final DofusRoomPreviewProcessor dofusRoomPreviewProcessor;

    public DofusRoomPreviewMapper(DiscordTranslator translator, DofusRoomPreviewProcessor dofusRoomPreviewProcessor){
        this.translator = translator;
        this.dofusRoomPreviewProcessor = dofusRoomPreviewProcessor;
    }

    public void decorateSpec(LegacyMessageCreateSpec spec, PreviewDto preview, Language language){
        spec.setEmbed(embedSpec -> embedSpec.setTitle(preview.getData().getName())
                .setDescription(translator.getLabel(language,
                        preview.isPrivate() ? "dofusroom.private.made_by" : "dofusroom.public.made_by",
                        preview.getData().getLevel(), preview.getData().getAuthor()))
                .setThumbnail("https://i.imgur.com/kwc2f0J.png")
                .setColor(Color.of(16628048))
                .setFooter(translator.getLabel(language, "dofusroom.generated"), null)
                .setUrl(preview.isPrivate() ? Constants.DOFUS_ROOM_BUILD_PRIVATE_URL_REFERRER
                                .replaceFirst("\\{}", preview.getToken()).replace("{}", preview.getId()) :
                        Constants.DOFUS_ROOM_BUILD_PUBLIC_URL_REFERRER.replace("{}", preview.getId()))
                .setImage("attachment://" + ATTACHMENT_FILENAME))
                .addFile(ATTACHMENT_FILENAME, createBuild(preview));
    }

    private InputStream createBuild(PreviewDto preview){
        StuffDto stuff = preview.getData().getItems();
        Optional<RingDto> rings = Optional.ofNullable(stuff.getRings());
        Optional<TrophusDto> trophus = Optional.ofNullable(stuff.getTrophus());
        MBFImage template = dofusRoomPreviewProcessor.getTemplate(preview.getData().getCharacter());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.AMULET, stuff.getAmulet());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TOP_RING, rings.map(RingDto::getTop).orElse(null));
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.BOTTOM_RING, rings.map(RingDto::getBottom).orElse(null));
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.SHIELD, stuff.getShield());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.WEAPON, stuff.getWeapon());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.CREATURE, stuff.getCreature());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.HAT, stuff.getHat());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.CAPE, stuff.getCape());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.BELT, stuff.getBelt());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.BOOTS, stuff.getBoots());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_1, trophus.map(TrophusDto::getFirst).orElse(null));
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_2, trophus.map(TrophusDto::getSecond).orElse(null));
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_3, trophus.map(TrophusDto::getThird).orElse(null));
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_4, trophus.map(TrophusDto::getFourth).orElse(null));
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_5, trophus.map(TrophusDto::getFifth).orElse(null));
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_6, trophus.map(TrophusDto::getSixth).orElse(null));
        return dofusRoomPreviewProcessor.getInputStream(template);
    }
}