package com.feed_the_beast.ftbl.api.info;

import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoPageTheme extends IJsonSerializable
{
    @SideOnly(Side.CLIENT)
    int getBackgroundColor();

    @SideOnly(Side.CLIENT)
    int getTextColor();

    @SideOnly(Side.CLIENT)
    boolean getUseUnicodeFont();
}
