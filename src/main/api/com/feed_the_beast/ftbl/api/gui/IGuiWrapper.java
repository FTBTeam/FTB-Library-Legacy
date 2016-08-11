package com.feed_the_beast.ftbl.api.gui;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 12.06.2016.
 */
@SideOnly(Side.CLIENT)
public interface IGuiWrapper
{
    GuiLM getWrappedGui();
}