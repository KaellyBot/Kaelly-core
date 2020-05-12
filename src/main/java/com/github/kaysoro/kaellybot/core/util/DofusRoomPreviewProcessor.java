package com.github.kaysoro.kaellybot.core.util;

import com.github.kaysoro.kaellybot.core.payload.dofusroom.ItemDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openimaj.image.ImageUtilities;
import org.openimaj.image.MBFImage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.Optional;

@Component
public class DofusRoomPreviewProcessor {

    private static final Log LOGGER = LogFactory.getLog(DofusRoomPreviewProcessor.class);

    private static final String TEMPLATE_PATH = "/dofusroom/templates/";
    private static final String ITEM_PATH = "/dofusroom/items/";

    private final String templateDirectory;
    private final String itemDirectory;

    public DofusRoomPreviewProcessor(@Value("${assets.directory}") String baseAssetsDirectory){
        templateDirectory = baseAssetsDirectory + TEMPLATE_PATH;
        itemDirectory = baseAssetsDirectory + ITEM_PATH;
    }

    private MBFImage loadImage(String basePath, String fileName, boolean isTransparent) {
        try {
            File file = new File(basePath + fileName + ".png");
            if (!file.exists()) {
                LOGGER.warn("Image with the following path does not exist: " + basePath + fileName + ".png");
                file = new File(basePath + "default.png");
            }
            return isTransparent ? ImageUtilities.readMBFAlpha(file) : ImageUtilities.readMBF(file);
        } catch(IOException e){
            LOGGER.error(e);
            return MBFImage.randomImage(1, 1);
        }
    }

    public InputStream getInputStream(MBFImage image){
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(ImageUtilities.createBufferedImageForDisplay(image), "png", os);
            return new ByteArrayInputStream(os.toByteArray());
        } catch(IOException e){
            LOGGER.error(e);
            return InputStream.nullInputStream();
        }
    }

    public MBFImage getTemplate(String character){
        return loadImage(templateDirectory, StringUtils.stripAccents(character.toLowerCase()).trim(), false);
    }

    public void drawItem(MBFImage template, ItemPosition itemPosition, ItemDto item){
        MBFImage imageItem = loadImage(itemDirectory, Optional.ofNullable(item).map(ItemDto::getId)
                .orElse(itemPosition.getPlaceholder()), true);
        template.drawImage(imageItem, itemPosition.getX(), itemPosition.getY());
    }

    @Getter
    @AllArgsConstructor
    public enum ItemPosition {
        AMULET     (4  , 4  , "amulet_placeholder"  ),
        TOP_RING   (4  , 88 , "ring_placeholder"    ),
        BOTTOM_RING(4  , 172, "ring_placeholder"    ),
        SHIELD     (4  , 256, "shield_placeholder"  ),
        WEAPON     (172, 256, "weapon_placeholder"  ),
        CREATURE   (256, 256, "creature_placeholder"),
        HAT        (424, 4  , "hat_placeholder"     ),
        CAPE       (424, 88 , "cape_placeholder"    ),
        BELT       (424, 172, "hat_placeholder"     ),
        BOOTS      (424, 256, "boots_placeholder"   ),
        TROPHUS_1  (4  , 340, "trophus_placeholder" ),
        TROPHUS_2  (88 , 340, "trophus_placeholder" ),
        TROPHUS_3  (172, 340, "trophus_placeholder" ),
        TROPHUS_4  (256, 340, "trophus_placeholder" ),
        TROPHUS_5  (340, 340, "trophus_placeholder" ),
        TROPHUS_6  (424, 340, "trophus_placeholder" );

        private final int x;
        private final int y;
        private final String placeholder;
    }
}
