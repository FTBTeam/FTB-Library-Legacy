package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoHrLine extends EmptyInfoPageLine
{
    public final int height;
    public final int color;

    public InfoHrLine(int h, int c)
    {
        height = h;
        color = c;
    }

    public InfoHrLine(JsonElement e)
    {
        JsonObject o = e.getAsJsonObject();
        height = o.has("height") ? Math.max(1, o.get("height").getAsInt()) : 1;
        color = o.has("color") ? LMColorUtils.deserialize(o.get("color")) : 0;
    }

    @Override
    public Widget createWidget(GuiBase gui, Panel parent)
    {
        return new WidgetInfoHr(parent);
    }

    @Override
    public IInfoTextLine copy(InfoPage page)
    {
        return new InfoHrLine(height, color);
    }

    @Override
    public JsonElement getJson()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive("hr"));
        o.add("height", new JsonPrimitive(height));
        o.add("color", LMColorUtils.serialize(color));
        return o;
    }

    private class WidgetInfoHr extends Widget
    {
        private WidgetInfoHr(Panel parent)
        {
            super(0, 1, parent.getWidth(), height + 2);
        }

        @Override
        public void renderWidget(GuiBase gui)
        {
            LMColorUtils.GL_COLOR.set(color == 0 ? gui.getTextColor() : color);
            GuiHelper.drawBlankRect(getAX(), getAY() + 1, getWidth(), height);
            GlStateManager.color(1F, 1F, 1F, 1F);
        }
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }
}