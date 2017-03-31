package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.gui.EnumDirection;
import com.feed_the_beast.ftbl.lib.gui.PanelLM;
import com.feed_the_beast.ftbl.lib.gui.WidgetLM;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiInfo;
import com.feed_the_beast.ftbl.lib.util.LMColorUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.client.renderer.GlStateManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 23.03.2016.
 */
public class InfoListLine extends EmptyInfoPageLine
{
    private final List<IInfoTextLine> textLines;
    private final boolean ordered;

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

            ordered = o.has("ordered") && o.get("ordered").getAsBoolean();
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

            ordered = false;
        }
    }

    public InfoListLine(List<IInfoTextLine> l, boolean o)
    {
        textLines = l;
        ordered = o;
    }

    @Override
    public IWidget createWidget(IGui gui, IPanel parent)
    {
        return new PanelList((GuiInfo) gui, parent.hasFlag(IPanel.FLAG_UNICODE_FONT));
    }

    @Override
    public IInfoTextLine copy(InfoPage page)
    {
        InfoListLine line = new InfoListLine(new ArrayList<>(textLines.size()), ordered);
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
        o.add("ordered", new JsonPrimitive(ordered));

        JsonArray a = new JsonArray();

        for(IInfoTextLine line : textLines)
        {
            a.add(line.getJson());
        }

        o.add("list", a);
        return o;
    }

    private class PanelList extends PanelLM
    {
        private final GuiInfo gui;

        private PanelList(GuiInfo g, boolean unicodeFont)
        {
            super(0, 0, 0, 0);
            gui = g;
            addFlags(FLAG_DEFAULTS);

            if(unicodeFont)
            {
                addFlags(FLAG_UNICODE_FONT);
            }
        }

        @Override
        public void addWidgets()
        {
            setWidth(gui.panelText.getWidth() - (getAX() - gui.panelText.getAX()) - 8);

            for(IInfoTextLine line : textLines)
            {
                IWidget widget = line.createWidget(gui, this);
                widget.setX(widget.getX() + 8);
                add(widget);
            }

            updateWidgetPositions();
        }

        @Override
        public void updateWidgetPositions()
        {
            setHeight(alignWidgets(EnumDirection.VERTICAL));
        }

        @Override
        protected void renderWidget(IGui gui, IWidget widget, int ax, int ay, int w, int h)
        {
            widget.renderWidget(gui);

            if(widget.getClass() != WidgetLM.class && !(widget instanceof PanelList))
            {
                LMColorUtils.GL_COLOR.set(gui.getTextColor());
                GuiInfo.TEX_BULLET.draw(ax + 1, widget.getAY() + 3, 4, 4);
                GlStateManager.color(1F, 1F, 1F, 1F);
            }
        }
    }
}