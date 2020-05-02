package com.github.kaysoro.kaellybot.core.mapper;

import com.github.kaysoro.kaellybot.core.model.constant.Constants;
import com.github.kaysoro.kaellybot.core.model.constant.Language;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.PreviewDto;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.RingDto;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.StuffDto;
import com.github.kaysoro.kaellybot.core.payload.dofusroom.TrophusDto;
import com.github.kaysoro.kaellybot.core.util.ImageProcessor;
import com.github.kaysoro.kaellybot.core.util.Translator;
import discord4j.core.spec.MessageCreateSpec;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.springframework.stereotype.Component;
import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.Optional;

@Component
public class DofusRoomPreviewMapper {

    private static final String ATTACHMENT_FILENAME = Constants.NAME + "_" + "Dofusroom_Preview.png";
    private Translator translator;
    private ImageProcessor imageProcessor;

    public DofusRoomPreviewMapper(Translator translator, ImageProcessor imageProcessor){
        this.translator = translator;
        this.imageProcessor = imageProcessor;
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
        try {
            StuffDto stuff = preview.getData().getItems();
            MBFImage template = ImageUtilities.readMBF(new File("C:\\Users\\steve\\Desktop\\assets\\dofusroom\\templates\\xelor.png"));

            Optional.ofNullable(stuff.getAmulet()).ifPresent(amulet -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + amulet.getId() + ".png"));
                    template.drawImage(item, 4, 4);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getRings()).map(RingDto::getTop).ifPresent(ring -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + ring.getId() + ".png"));
                    template.drawImage(item, 4, 88);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getRings()).map(RingDto::getBottom).ifPresent(ring -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + ring.getId() + ".png"));
                    template.drawImage(item, 4, 172);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getShield()).ifPresent(shield -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + shield.getId() + ".png"));
                    template.drawImage(item, 4, 256);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getWeapon()).ifPresent(weapon -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + weapon.getId() + ".png"));
                    template.drawImage(item, 172, 256);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getCreature()).ifPresent(creature -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + creature.getId() + ".png"));
                    template.drawImage(item, 256, 256);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getHat()).ifPresent(hat -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + hat.getId() + ".png"));
                    template.drawImage(item, 424, 4);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getCape()).ifPresent(cape -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + cape.getId() + ".png"));
                    template.drawImage(item, 424, 88);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getBelt()).ifPresent(belt -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + belt.getId() + ".png"));
                    template.drawImage(item, 424, 172);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getBoots()).ifPresent(boots -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + boots.getId() + ".png"));
                    template.drawImage(item, 424, 256);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getTrophus()).map(TrophusDto::getFirst).ifPresent(trophus -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + trophus.getId() + ".png"));
                    template.drawImage(item, 4, 340);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getTrophus()).map(TrophusDto::getSecond).ifPresent(trophus -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + trophus.getId() + ".png"));
                    template.drawImage(item, 88, 340);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getTrophus()).map(TrophusDto::getThird).ifPresent(trophus -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + trophus.getId() + ".png"));
                    template.drawImage(item, 172, 340);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getTrophus()).map(TrophusDto::getFourth).ifPresent(trophus -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + trophus.getId() + ".png"));
                    template.drawImage(item, 256, 340);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getTrophus()).map(TrophusDto::getFifth).ifPresent(trophus -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + trophus.getId() + ".png"));
                    template.drawImage(item, 340, 340);
                } catch(Exception e){}
            });
            Optional.ofNullable(stuff.getTrophus()).map(TrophusDto::getSixth).ifPresent(trophus -> {
                try {
                    MBFImage item = ImageUtilities.readMBFAlpha(new File(
                            "C:\\Users\\steve\\Desktop\\assets\\dofusroom\\items_80\\" + trophus.getId() + ".png"));
                    template.drawImage(item, 424, 340);
                } catch(Exception e){}
            });

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(ImageUtilities.createBufferedImageForDisplay(template), "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return InputStream.nullInputStream();
    }
}