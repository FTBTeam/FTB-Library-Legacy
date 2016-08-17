package com.feed_the_beast.ftbl.api.info.impl;

import com.feed_the_beast.ftbl.api.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public class InfoTextLineString implements IInfoTextLine
{
    private String text;

    public InfoTextLineString(String s)
    {
        text = s;
    }

    @Override
    public String getUnformattedText()
    {
        return text;
    }

    @Override
    @Nonnull
    @SideOnly(Side.CLIENT)
    public ButtonLM createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoTextLine(gui, text);
    }

    @Override
    public void fromJson(@Nonnull JsonElement e)
    {
        text = e.getAsString();
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(text);
    }
}
