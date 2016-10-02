package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.api.info.IGuiInfoPage;
import com.feed_the_beast.ftbl.api.info.IInfoTextLineProvider;
import com.feed_the_beast.ftbl.api.info.InfoTextLineProvider;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 20.03.2016.
 */
public class InfoExtendedTextLine extends EmptyInfoPageLine
{
    @InfoTextLineProvider("text_component")
    public static final IInfoTextLineProvider PROVIDER = (page, json) -> new InfoExtendedTextLine(null);
    public ITextComponent text;

    public InfoExtendedTextLine(@Nullable ITextComponent cc)
    {
        text = cc;
    }

    @Override
    @Nullable
    public String getUnformattedText()
    {
        return text == null ? null : text.getUnformattedText();
    }

    @Override
    public IWidget createWidget(GuiInfo gui, IGuiInfoPage page)
    {
        return new ButtonInfoExtendedTextLine(gui, this);
    }

    @Override
    public void fromJson(JsonElement e)
    {
        text = LMJsonUtils.deserializeTextComponent(e);
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return LMJsonUtils.serializeTextComponent(text);
    }
}