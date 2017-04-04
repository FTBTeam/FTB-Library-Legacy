package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.gui.EnumDirection;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.IStringSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoListLine extends EmptyInfoPageLine
{
    public enum Type implements IStringSerializable
    {
        SIMPLE("simple", EnumDirection.VERTICAL, false),
        UNORDERED("unordered", EnumDirection.VERTICAL, true),
        ORDERED("ordered", EnumDirection.VERTICAL, true),
        ORDERED_LETTER("ordered_letter", EnumDirection.VERTICAL, true),
        HORIZONTAL("horizontal", EnumDirection.HORIZONTAL, false);

        private static final Map<String, Type> MAP = new HashMap<>();

        static
        {
            for(Type t : values())
            {
                MAP.put(t.name, t);
            }
        }

        private final String name;
        public final EnumDirection direction;
        public final boolean hasBullet;

        Type(String s, EnumDirection d, boolean b)
        {
            name = s;
            direction = d;
            hasBullet = b;
        }

        @Override
        public String getName()
        {
            return name;
        }

        public static Type get(String s)
        {
            Type t = MAP.get(s);
            return t == null ? UNORDERED : t;
        }
    }

    private final List<IInfoTextLine> textLines;
    private final Type type;
    private final int spacing;

    public InfoListLine(InfoPage p, JsonElement json)
    {
        textLines = new ArrayList<>();

        if(json.isJsonObject())
        {
            JsonObject o = json.getAsJsonObject();

            if(o.has("list"))
            {
                for(JsonElement element : o.get("list").getAsJsonArray())
                {
                    IInfoTextLine line = InfoPageHelper.createLine(p, element);

                    if(line != null)
                    {
                        textLines.add(line);
                    }
                }
            }

            type = o.has("type") ? Type.get(o.get("type").getAsString()) : Type.UNORDERED;
            spacing = o.has("spacing") ? o.get("spacing").getAsInt() : 0;
        }
        else
        {
            for(JsonElement element : json.getAsJsonArray())
            {
                IInfoTextLine line = InfoPageHelper.createLine(p, element);

                if(line != null)
                {
                    textLines.add(line);
                }
            }

            type = Type.UNORDERED;
            spacing = 0;
        }
    }

    public InfoListLine(List<IInfoTextLine> l, Type t, int s)
    {
        textLines = l;
        type = t;
        spacing = s;
    }

    @Override
    public Widget createWidget(GuiBase gui, Panel parent)
    {
        return new PanelList((GuiInfo) gui, parent.hasFlag(Panel.FLAG_UNICODE_FONT));
    }

    @Override
    public IInfoTextLine copy(InfoPage page)
    {
        InfoListLine line = new InfoListLine(new ArrayList<>(textLines.size()), type, spacing);
        for(IInfoTextLine line1 : textLines)
        {
            line.textLines.add(line1.copy(page));
        }
        return line;
    }

    @Override
    public JsonElement getJson()
    {
        JsonObject o = new JsonObject();
        o.add("id", new JsonPrimitive("list"));
        o.add("type", new JsonPrimitive(type.name));

        JsonArray a = new JsonArray();

        for(IInfoTextLine line : textLines)
        {
            a.add(line.getJson());
        }

        o.add("list", a);
        return o;
    }

    @Override
    public boolean isEmpty()
    {
        for(IInfoTextLine line : textLines)
        {
            if(!line.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    private class PanelList extends Panel
    {
        private final GuiInfo gui;

        private PanelList(GuiInfo g, boolean unicodeFont)
        {
            super(0, 0, 0, 0);
            gui = g;
            //addFlags(FLAG_DEFAULTS);

            if(unicodeFont)
            {
                addFlags(FLAG_UNICODE_FONT);
            }
        }

        @Override
        public void addWidgets()
        {
            setWidth(gui.panelText.width - (getAX() - gui.panelText.getAX()) - (type.hasBullet ? 8 : 0));

            for(IInfoTextLine line : textLines)
            {
                Widget widget = line.createWidget(gui, this);

                if(type.hasBullet)
                {
                    widget.setX(widget.posX + 8);
                }

                add(widget);
            }

            updateWidgetPositions();
        }

        @Override
        public void updateWidgetPositions()
        {
            setHeight(alignWidgets(type.direction, 0, spacing, 0));

            if(type.direction.isHorizontal())
            {
                int h = 0;

                for(Widget w : getWidgets())
                {
                    h = Math.max(h, w.height);
                }

                setHeight(h);
            }
        }

        @Override
        protected void renderWidget(GuiBase gui, Widget widget, int ax, int ay, int w, int h)
        {
            widget.renderWidget(gui);

            if(type.hasBullet && widget.getClass() != Widget.class && !(widget instanceof PanelList))
            {
                LMColorUtils.GL_COLOR.set(gui.getTextColor());
                GuiInfo.TEX_BULLET.draw(ax + 1, widget.getAY() + 3, 4, 4);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }
        }
    }
}