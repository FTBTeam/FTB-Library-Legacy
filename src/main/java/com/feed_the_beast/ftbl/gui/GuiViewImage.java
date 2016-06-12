package com.feed_the_beast.ftbl.gui;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.api.client.gui.GuiLM;
import com.feed_the_beast.ftbl.util.TextureCoords;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiViewImage extends GuiLM
{
    public final TextureCoords texCoords;

    public GuiViewImage(TextureCoords t)
    {
        texCoords = t;
    }

    @Override
    public void onInit()
    {
        setFullscreen();
    }

    @Override
    public void addWidgets()
    {
    }

    @Override
    public void mousePressed(GuiLM gui, MouseButton b)
    {
        closeGui();
    }

    @Override
    public void drawBackground()
    {
        super.drawBackground();

        if(texCoords != null && texCoords.isValid())
        {
            FTBLibClient.setTexture(texCoords.texture);

            double w = texCoords.width;
            double h = texCoords.height;

            if(w > widthW)
            {
                w = widthW;
                h = texCoords.getHeight(w);
            }

            if(h > heightW)
            {
                h = heightW;
                w = texCoords.getWidth(h);
            }

            GuiLM.render(texCoords, (widthW - w) / 2, (heightW - h) / 2, w, h);
        }
    }
}