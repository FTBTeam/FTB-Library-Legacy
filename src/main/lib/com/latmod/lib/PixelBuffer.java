package com.latmod.lib;

import com.latmod.lib.util.LMColorUtils;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class PixelBuffer implements IPixelBuffer
{
    private final int width, height;
    private final int[] pixels;

    public PixelBuffer(int w, int h)
    {
        width = w;
        height = h;
        pixels = new int[w * h];
    }

    public PixelBuffer(BufferedImage img)
    {
        this(img.getWidth(), img.getHeight());
        img.getRGB(0, 0, width, height, pixels, 0, width);
    }

    @Override
    public int getWidth()
    {
        return width;
    }

    @Override
    public int getHeight()
    {
        return height;
    }

    @Override
    public int[] getPixels()
    {
        return pixels;
    }

    @Override
    public void setPixels(int[] p)
    {
        if(p.length == pixels.length)
        {
            System.arraycopy(p, 0, pixels, 0, pixels.length);
        }
    }

    @Override
    public void setRGB(int x, int y, int col)
    {
        pixels[x + y * width] = col;
    }

    @Override
    public int getRGB(int x, int y)
    {
        return pixels[x + y * width];
    }

    @Override
    public void setRGB(int startX, int startY, int w, int h, int[] rgbArray)
    {
        int off = -1;
        for(int y = startY; y < startY + h; y++)
        {
            for(int x = startX; x < startX + w; x++)
            {
                setRGB(x, y, rgbArray[++off]);
            }
        }
    }

    @Override
    public void setRGB(int startX, int startY, IPixelBuffer buffer)
    {
        setRGB(startX, startY, buffer.getWidth(), buffer.getHeight(), buffer.getPixels());
    }

    @Override
    public int[] getRGB(int startX, int startY, int w, int h, @Nullable int[] p)
    {
        if(p == null || p.length != w * h)
        {
            p = new int[w * h];
        }

        int off = -1;
        w += startX;
        h += startY;
        for(int y = startY; y < h; y++)
        {
            for(int x = startX; x < w; x++)
            {
                p[++off] = getRGB(x, y);
            }
        }

        return p;
    }

    @Override
    public BufferedImage toImage(int type)
    {
        BufferedImage image = new BufferedImage(width, height, type);
        image.setRGB(0, 0, width, height, pixels, 0, width);
        return image;
    }

    public void fill(int col)
    {
        Arrays.fill(pixels, col);
    }

    public void fill(int startX, int startY, int w, int h, int col)
    {
        for(int y = startY; y < startY + h; y++)
        {
            for(int x = startX; x < startX + w; x++)
            {
                setRGB(x, y, col);
            }
        }
    }

    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        else if(o == this)
        {
            return true;
        }
        else if(o instanceof PixelBuffer)
        {
            PixelBuffer b = (PixelBuffer) o;
            if(width == b.width && height == b.height)
            {
                for(int i = 0; i < pixels.length; i++)
                {
                    if(pixels[i] != b.pixels[i])
                    {
                        return false;
                    }
                }

                return true;
            }
        }
        return false;
    }

    public int hashCode()
    {
        return Arrays.hashCode(getPixels());
    }

    public PixelBuffer copy()
    {
        PixelBuffer b = new PixelBuffer(width, height);
        System.arraycopy(pixels, 0, b.pixels, 0, pixels.length);
        return b;
    }

    @Override
    public IPixelBuffer getSubimage(int x, int y, int w, int h)
    {
        PixelBuffer b = new PixelBuffer(w, h);
        getRGB(x, y, w, h, b.pixels);
        return b;
    }

    public void addHSB(float h, float s, float b)
    {
        LMColorUtils.addHSB(pixels, h, s, b);
    }
}