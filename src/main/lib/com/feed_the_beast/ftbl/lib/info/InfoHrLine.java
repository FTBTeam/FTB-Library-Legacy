package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoHrLine extends EmptyInfoPageLine
{
    public final int height;
    public final Color4I color;

    public InfoHrLine(int h, Color4I c)
    {
        height = h;
        color = c;
    }

    public InfoHrLine(JsonElement e)
    {
        JsonObject o = e.getAsJsonObject();
        height = o.has("height") ? Math.max(1, o.get("height").getAsInt()) : 1;
        color = o.has("color") ? new Color4I(0xFF000000 | LMColorUtils.deserialize(o.get("color"))) : Color4I.NONE;
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
        o.add("color", color.toJson());
        return o;
    }

    private class WidgetInfoHr extends Widget
    {
        private WidgetInfoHr(Panel parent)
        {
            super(0, 1, parent.width, InfoHrLine.this.height + 2);
        }

        @Override
        public void renderWidget(GuiBase gui)
        {
            GuiHelper.drawBlankRect(getAX(), getAY() + 1, width, height, color.hasColor() ? color : gui.getContentColor());
        }
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }
}