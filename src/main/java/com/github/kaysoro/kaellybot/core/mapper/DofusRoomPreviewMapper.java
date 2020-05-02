package com.github.kaysoro.kaellybot.core.mapper;

import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.PreviewDto;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.StuffDto;
import com.github.kaysoro.kaellybot.core.util.DofusRoomPreviewProcessor;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.spec.MessageCreateSpec;
import static com.github.kaysoro.kaellybot.core.util.DofusRoomPreviewProcessor.ItemPosition;
import org.openimaj.image.MBFImage;
import org.springframework.stereotype.Component;
import java.awt.*;
import java.io.*;

@Component
public class DofusRoomPreviewMapper {

    private static final String ATTACHMENT_FILENAME = Constants.NAME + "_" + "Dofusroom_Preview.png";
    private Translator translator;
    private DofusRoomPreviewProcessor dofusRoomPreviewProcessor;

    public DofusRoomPreviewMapper(Translator translator, DofusRoomPreviewProcessor dofusRoomPreviewProcessor){
        this.translator = translator;
        this.dofusRoomPreviewProcessor = dofusRoomPreviewProcessor;
    }

    public void decorateSpec(MessageCreateSpec spec, PreviewDto preview, Language language){
        spec.setEmbed(embedSpec -> embedSpec.setTitle(preview.getData().getName())
                .setDescription(translator.getLabel(language, "dofusroom.made_by",
                        preview.getData().getLevel(), preview.getData().getAuthor()))
                .setThumbnail("https://i.imgur.com/kwc2f0J.png")
                .setColor(Color.decode("#fdb950"))
                .setFooter(translator.getLabel(language, "dofusroom.generated"), null)
                .setUrl(Constants.DOFUS_ROOM_BUILD_URL_REFERRER.replace("{}", preview.getId()))
                .setImage("attachment://" + ATTACHMENT_FILENAME))
                .addFile(ATTACHMENT_FILENAME, createBuild(preview));
    }

    private InputStream createBuild(PreviewDto preview){
        StuffDto stuff = preview.getData().getItems();
        MBFImage template = dofusRoomPreviewProcessor.getTemplate(preview.getData().getCharacter());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.AMULET, stuff.getAmulet());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TOP_RING, stuff.getRings().getTop());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.BOTTOM_RING, stuff.getRings().getBottom());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.SHIELD, stuff.getShield());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.WEAPON, stuff.getWeapon());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.CREATURE, stuff.getCreature());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.HAT, stuff.getHat());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.CAPE, stuff.getCape());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.BELT, stuff.getBelt());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.BOOTS, stuff.getBoots());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_1, stuff.getTrophus().getFirst());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_2, stuff.getTrophus().getSecond());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_3, stuff.getTrophus().getThird());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_4, stuff.getTrophus().getFourth());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_5, stuff.getTrophus().getFifth());
        dofusRoomPreviewProcessor.drawItem(template, ItemPosition.TROPHUS_6, stuff.getTrophus().getSixth());
        return dofusRoomPreviewProcessor.getInputStream(template);
    }
}