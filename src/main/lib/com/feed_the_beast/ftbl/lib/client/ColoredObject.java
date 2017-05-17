package com.feed_the_beast.ftbl.lib.client;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author LatvianModder
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
    public JsonObject getJson()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive("colored"));

        if(color.hasColor())
        {
            o.add("color", ColorUtils.serialize(color.rgba()));
            if(color.alpha() != 255)
            {
                o.add("color_alpha", new JsonPrimitive(color.alpha()));
            }
        }
        o.add("parent", parent.getJson());
        return o;
    }
}