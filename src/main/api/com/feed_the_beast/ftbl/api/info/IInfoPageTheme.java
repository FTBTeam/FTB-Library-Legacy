package com.feed_the_beast.ftbl.api.info;

import net.minecraft.util.IJsonSerializable;

/**
 * Created by LatvianModder on 08.08.2016.
 */
public interface IInfoPageTheme extends IJsonSerializable
{
    int getBackgroundColor();

    int getTextColor();

    boolean getUseUnicodeFont();
}
