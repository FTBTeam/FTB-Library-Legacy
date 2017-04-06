package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.gui.IImageProvider;
import com.feed_the_beast.ftbl.lib.LangKey;
import net.minecraft.util.IStringSerializable;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IRedstoneMode extends IStringSerializable
{
    IImageProvider getIcon();

    boolean isActive(boolean rsHigh);

    LangKey getLangKey();
}