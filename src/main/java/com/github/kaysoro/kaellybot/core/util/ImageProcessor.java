package com.github.kaysoro.kaellybot.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

@Component
public class ImageProcessor {

    private static final Log LOGGER = LogFactory.getLog(ImageProcessor.class);

    public BufferedImage loadImage(String path, String defaultPath) throws IOException {
        File file = new File(path);

        if (! file.exists()){
            LOGGER.error("Image with the following path does not exist: " + path);

            if (defaultPath != null) {
                file = new File(defaultPath);
                if (!file.exists()) throw new FileNotFoundException(path);
            }
        }

        return ImageIO.read(file);
    }
}
