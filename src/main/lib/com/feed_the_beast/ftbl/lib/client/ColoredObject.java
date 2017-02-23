package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by LatvianModder on 23.02.2017.
 */
public class ColoredObject implements IDrawableObject
{
    public final IDrawableObject parent;
    public final Color4I color;

    public ColoredObject(IDrawableObject p, Color4I c)
    {
        parent = p;
        color = c;
    }

    public ColoredObject(IDrawableObject p, int c)
    {
        parent = p;
        color = new Color4I();
        color.set(c);
    }

    @Override
    public void draw(int x, int y, int w, int h)
    {
        LMColorUtils.GL_COLOR.set(color);

        if(parent == ImageProvider.NULL)
        {
            GuiHelper.drawBlankRect(x, y, w, h);
        }
        else
        {
            parent.draw(x, y, w, h);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }
}