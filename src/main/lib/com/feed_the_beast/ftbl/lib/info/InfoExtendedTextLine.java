package com.feed_the_beast.ftbl.lib.info;

import com.feed_the_beast.ftbl.api.info.IInfoTextLine;
import com.feed_the_beast.ftbl.lib.gui.ExtendedTextField;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.google.gson.JsonElement;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 20.03.2016.
 */
public class InfoExtendedTextLine extends EmptyInfoPageLine
{
    public final ITextComponent textComponent;

    public InfoExtendedTextLine(@Nullable ITextComponent cc)
    {
        textComponent = cc;
    }

    public InfoExtendedTextLine(JsonElement json)
    {
        textComponent = LMJsonUtils.deserializeTextComponent(json);
    }

    @Override
    public String getUnformattedText()
    {
        return textComponent == null ? "" : textComponent.getUnformattedText();
    }

    @Override
    public Widget createWidget(GuiBase gui, Panel parent)
    {
        return new ExtendedTextField(0, 0, parent.getWidth(), -1, gui.getFont(), textComponent);
    }

    @Override
    public IInfoTextLine copy(InfoPage page)
    {
        return new InfoExtendedTextLine(textComponent.createCopy());
    }

    @Override
    public JsonElement getJson()
    {
        return LMJsonUtils.serializeTextComponent(textComponent);
    }
}