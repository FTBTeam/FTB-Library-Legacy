package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by LatvianModder on 24.02.2017.
 */
public class TexturelessRectangle implements IDrawableObject
{
    private int color, lineColor;
    private boolean roundEdges = false;

    public TexturelessRectangle(int col)
    {
        color = col;
    }

    public TexturelessRectangle setLineColor(int col)
    {
        lineColor = col;
        return this;
    }

    public TexturelessRectangle setRoundEdges()
    {
        roundEdges = true;
        return this;
    }

    @Override
    public void draw(int x, int y, int w, int h)
    {
        LMColorUtils.GL_COLOR.set(color);

        if(roundEdges || lineColor != 0)
        {
            GuiHelper.drawBlankRect(x + 1, y + 1, w - 2, h - 2);

            if(lineColor != 0)
            {
                LMColorUtils.GL_COLOR.set(lineColor);
            }

            GuiHelper.drawHollowRect(x, y, w, h, roundEdges);
        }
        else
        {
            GuiHelper.drawBlankRect(x, y, w, h);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}