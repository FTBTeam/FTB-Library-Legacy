package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class PlayerHeadImage implements IImageProvider
{
    private final String username;

    public PlayerHeadImage(String u)
    {
        username = u;
    }

    @Override
    public ResourceLocation getImage()
    {
        return FTBLibClient.getSkinTexture(username);
    }

    @Override
    public ITextureObject bindTexture()
    {
        return FTBLibClient.bindTexture(getImage());
    }

    @Override
    public void draw(int x, int y, int w, int h, Color4I col)
    {
        bindTexture();

        if(!col.hasColor())
        {
            col = Color4I.WHITE;
        }

        GuiHelper.drawTexturedRect(x, y, w, h, col, 0.125D, 0.125D, 0.25D, 0.25D);
        GuiHelper.drawTexturedRect(x, y, w, h, col, 0.625D, 0.125D, 0.75D, 0.25D);
    }

    @Override
    public double getMinU()
    {
        return 0.125D;
    }

    @Override
    public double getMinV()
    {
        return 0.125D;
    }

    @Override
    public double getMaxU()
    {
        return 0.25D;
    }

    @Override
    public double getMaxV()
    {
        return 0.25D;
    }
}