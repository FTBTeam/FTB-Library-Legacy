package com.feed_the_beast.ftbl.lib.client;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;

/**
 * Created by LatvianModder on 29.08.2016.
 */
public interface IPixelBuffer
{
    int getWidth();

    int getHeight();

    int[] getPixels();

    void setPixels(int[] pixels);

    int getRGB(int x, int y);

    int[] getRGB(int startX, int startY, int w, int h, @Nullable int[] rgbArray);

    void setRGB(int x, int y, int col);

    void setRGB(int startX, int startY, int w, int h, int[] rgbArray);

    void setRGB(int startX, int startY, IPixelBuffer buffer);

    BufferedImage toImage(int type);

    IPixelBuffer getSubimage(int x, int y, int w, int h);
}