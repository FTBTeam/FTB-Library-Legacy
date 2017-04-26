package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

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
        this(p, new Color4I(true, c));
    }

    @Override
    public void draw(int x, int y, int w, int h, Color4I col)
    {
        Color4I col1 = col.hasColor() ? col : color;

        if(parent == ImageProvider.NULL)
        {
            GuiHelper.drawBlankRect(x, y, w, h, col1);
        }
        else
        {
            parent.draw(x, y, w, h, col1);
        }

        GlStateManager.color(1F, 1F, 1F, 1F);
    }

    @Override
    public ResourceLocation getImage()
    {
        return new ResourceLocation("colored_object:" + parent.getImage());
    }
}