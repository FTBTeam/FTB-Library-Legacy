package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IGui;
import com.feed_the_beast.ftbl.api.gui.IPanel;
import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.gui.PanelLM;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

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

    public InfoListLine(List<IInfoTextLine> l, boolean o)
    {
        textLines = l;
        ordered = o;
    }

    @Override
    public IWidget createWidget(IGui gui, IPanel parent)
    {
        return new PanelList(gui);
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
        private final IGui gui;

        private PanelList(IGui g)
        {
            super(0, 0, 0, 0);
            gui = g;
        }

        @Override
        public void updateWidgetPositions()
        {
            setHeight(0);

            for(IWidget w : getWidgets())
            {
                w.setY(getHeight());
                setHeight(getHeight() + w.getHeight());
            }
        }

        @Override
        public void addWidgets()
        {
            for(IInfoTextLine line : textLines)
            {
                add(line.createWidget(gui, this));
            }

            updateWidgetPositions();
        }
    }
}