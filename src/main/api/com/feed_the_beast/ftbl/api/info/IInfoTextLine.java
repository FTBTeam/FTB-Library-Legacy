package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.gui.IWidget;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.util.IJsonSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoTextLine extends IJsonSerializable
{
    @Nullable
    String getUnformattedText();

    IWidget createWidget(GuiInfo gui, IGuiInfoPage page);
}
