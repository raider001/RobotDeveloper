package com.kalynx.robotdeveloper.graphic;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;

public class ImageFactory {

    public Image getImage(String image) {
        try {
           Enumeration<?> stuff =getClass().getClassLoader().getResources("./");
            URL url =getClass().getResource("/images/" + image);
            Image i = ImageIO.read(url);
            return i;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
