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
    public int color, lineColor;
    public boolean roundEdges = false;

    public TexturelessRectangle(int col)
    {
        color = col;
    }

    public TexturelessRectangle setLineColor(int col)
    {
        lineColor = col;
        return this;
    }

    public TexturelessRectangle setRoundEdges(boolean v)
    {
        roundEdges = v;
        return this;
    }

    public TexturelessRectangle copy()
    {
        TexturelessRectangle t = new TexturelessRectangle(color);
        t.lineColor = lineColor;
        t.roundEdges = roundEdges;
        return t;
    }

    @Override
    public void draw(int x, int y, int w, int h)
    {
        if(roundEdges || lineColor != 0)
        {
            if(color != 0)
            {
                LMColorUtils.GL_COLOR.set(color);
                GuiHelper.drawBlankRect(x + 1, y + 1, w - 2, h - 2);
            }

            if(lineColor != 0)
            {
                LMColorUtils.GL_COLOR.set(lineColor);
            }

            GuiHelper.drawHollowRect(x, y, w, h, roundEdges);
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
        else if(color != 0)
        {
            LMColorUtils.GL_COLOR.set(color);
            GuiHelper.drawBlankRect(x, y, w, h);
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }
}