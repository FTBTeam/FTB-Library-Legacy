package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.util.LMJsonUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 20.03.2016.
 */
public class InfoExtendedTextLine implements IInfoTextLine
{
    private ITextComponent text;
    private ClickEvent clickEvent;
    private List<ITextComponent> hover;

    public InfoExtendedTextLine(@Nullable ITextComponent cc)
    {
        text = cc;

        if(text != null)
        {
            clickEvent = text.getStyle().getClickEvent();

            HoverEvent hoverEvent = text.getStyle().getHoverEvent();
            if(hoverEvent != null && hoverEvent.getAction() == HoverEvent.Action.SHOW_TEXT)
            {
                hover = Collections.singletonList(hoverEvent.getValue());
            }
        }
    }

    @Nullable
    public ITextComponent getText()
    {
        return text;
    }

    @Override
    @Nullable
    public String getUnformattedText()
    {
        return text == null ? null : text.getUnformattedText();
    }

    @Override
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoExtendedTextLine(gui, this);
    }

    @Nullable
    public List<ITextComponent> getHover()
    {
        return hover;
    }

    public void setHover(@Nullable List<ITextComponent> h)
    {
        if(h == null || h.isEmpty())
        {
            hover = null;
        }
        else
        {
            hover = new ArrayList<>(h.size());
            hover.addAll(h);
        }
    }

    public ClickEvent getClickEvent()
    {
        return clickEvent;
    }

    @Override
    public void fromJson(JsonElement e)
    {
        JsonObject o = e.getAsJsonObject();

        text = o.has("text") ? LMJsonUtils.deserializeTextComponent(o.get("text")) : null;

        clickEvent = null;
        if(o.has("click"))
        {
            JsonObject o1 = o.get("click").getAsJsonObject();
            JsonPrimitive j = o1.getAsJsonPrimitive("action");
            ClickEvent.Action action = j == null ? null : ClickEvent.Action.getValueByCanonicalName(j.getAsString());
            j = o1.getAsJsonPrimitive("value");
            String s = j == null ? null : j.getAsString();

            if(action != null && s != null)
            {
                clickEvent = new ClickEvent(action, s);
            }
        }

        if(o.has("hover"))
        {
            hover = new ArrayList<>();

            JsonElement e1 = o.get("hover");

            if(e1.isJsonPrimitive())
            {
                hover.add(LMJsonUtils.deserializeTextComponent(e1));
            }
            else
            {
                for(JsonElement e2 : o.get("hover").getAsJsonArray())
                {
                    hover.add(LMJsonUtils.deserializeTextComponent(e2));
                }
            }

            if(hover.isEmpty())
            {
                hover = null;
            }
        }
        else
        {
            hover = null;
        }
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        if(text != null)
        {
            o.add("text", LMJsonUtils.serializeTextComponent(text));
        }

        if(clickEvent != null)
        {
            JsonObject o1 = new JsonObject();
            o1.addProperty("action", clickEvent.getAction().getCanonicalName());
            o1.addProperty("value", clickEvent.getValue());
            o.add("click", o1);
        }

        if(hover != null && !hover.isEmpty())
        {
            if(hover.size() == 1)
            {
                o.add("hover", LMJsonUtils.serializeTextComponent(hover.get(0)));
            }
            else
            {
                JsonArray a = new JsonArray();
                for(ITextComponent c : hover)
                {
                    a.add(LMJsonUtils.serializeTextComponent(c));
                }

                o.add("hover", a);
            }
        }

        return o;
    }
}