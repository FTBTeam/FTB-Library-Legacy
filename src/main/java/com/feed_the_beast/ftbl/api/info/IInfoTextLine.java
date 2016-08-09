package com.feed_the_beast.ftbl.api.info;

import com.feed_the_beast.ftbl.api.client.gui.widgets.ButtonLM;
import com.feed_the_beast.ftbl.gui.GuiInfo;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoTextLine extends IJsonSerializable
{
    @Nullable
    String getUnformattedText();

    @SideOnly(Side.CLIENT)
    @Nonnull
    ButtonLM createWidget(GuiInfo gui, IGuiInfoPageTree page);
}
