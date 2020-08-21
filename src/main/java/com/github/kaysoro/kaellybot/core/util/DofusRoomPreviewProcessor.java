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

    private static final String AMULET_PLACEHOLDER   = "amulet_placeholder"  ;
    private static final String RING_PLACEHOLDER     = "ring_placeholder"    ;
    private static final String SHIELD_PLACEHOLDER   = "shield_placeholder"  ;
    private static final String WEAPON_PLACEHOLDER   = "weapon_placeholder"  ;
    private static final String CREATURE_PLACEHOLDER = "creature_placeholder";
    private static final String HAT_PLACEHOLDER      = "hat_placeholder"     ;
    private static final String CAPE_PLACEHOLDER     = "cape_placeholder"    ;
    private static final String BELT_PLACEHOLDER     = "belt_placeholder"    ;
    private static final String BOOTS_PLACEHOLDER    = "boots_placeholder"   ;
    private static final String TROPHUS_PLACEHOLDER  = "trophus_placeholder" ;

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
        AMULET     (4  , 4  , AMULET_PLACEHOLDER  ),
        TOP_RING   (4  , 88 , RING_PLACEHOLDER    ),
        BOTTOM_RING(4  , 172, RING_PLACEHOLDER    ),
        SHIELD     (4  , 256, SHIELD_PLACEHOLDER  ),
        WEAPON     (172, 256, WEAPON_PLACEHOLDER  ),
        CREATURE   (256, 256, CREATURE_PLACEHOLDER),
        HAT        (424, 4  , HAT_PLACEHOLDER     ),
        CAPE       (424, 88 , CAPE_PLACEHOLDER    ),
        BELT       (424, 172, BELT_PLACEHOLDER    ),
        BOOTS      (424, 256, BOOTS_PLACEHOLDER   ),
        TROPHUS_1  (4  , 340, TROPHUS_PLACEHOLDER ),
        TROPHUS_2  (88 , 340, TROPHUS_PLACEHOLDER ),
        TROPHUS_3  (172, 340, TROPHUS_PLACEHOLDER ),
        TROPHUS_4  (256, 340, TROPHUS_PLACEHOLDER ),
        TROPHUS_5  (340, 340, TROPHUS_PLACEHOLDER ),
        TROPHUS_6  (424, 340, TROPHUS_PLACEHOLDER );

        private final int x;
        private final int y;
        private final String placeholder;
    }
}
