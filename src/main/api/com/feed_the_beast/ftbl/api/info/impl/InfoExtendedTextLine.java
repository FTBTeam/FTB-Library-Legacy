package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPageTree;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.api.notification.ClickAction;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.json.LMJsonUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
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
    private ClickAction clickAction;
    private List<ITextComponent> hover;

    public InfoExtendedTextLine(ITextComponent cc)
    {
        text = cc;

        if(text != null)
        {
            ClickEvent clickEvent = text.getStyle().getClickEvent();

            if(clickEvent != null)
            {
                clickAction = ClickAction.from(clickEvent);
            }

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
    @Nonnull
    @SideOnly(Side.CLIENT)
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPageTree page)
    {
        return new ButtonInfoExtendedTextLine(gui, this);
    }

    public List<ITextComponent> getHover()
    {
        return hover;
    }

    public void setHover(List<ITextComponent> h)
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

    @SideOnly(Side.CLIENT)
    public ClickAction getClickAction()
    {
        return clickAction;
    }

    public void setClickAction(ClickAction a)
    {
        clickAction = a;
    }

    @Override
    public void fromJson(@Nonnull JsonElement e)
    {
        JsonObject o = e.getAsJsonObject();

        text = o.has("text") ? LMJsonUtils.deserializeTextComponent(o.get("text")) : null;

        if(o.has("click"))
        {
            clickAction = new ClickAction();
            clickAction.fromJson(o.get("click"));
        }
        else
        {
            clickAction = null;
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

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        JsonObject o = new JsonObject();
        if(text != null)
        {
            o.add("text", LMJsonUtils.serializeTextComponent(text));
        }

        if(clickAction != null)
        {
            o.add("click", clickAction.getSerializableElement());
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