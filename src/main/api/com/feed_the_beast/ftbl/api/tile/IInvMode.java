package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.lib.ILangKeyContainer;
import com.feed_the_beast.ftbl.lib.client.ITextureCoordsProvider;
import net.minecraft.util.IStringSerializable;

/**
 * Created by LatvianModder on 11.09.2016.
 */
public interface IInvMode extends IStringSerializable, ILangKeyContainer
{
    ITextureCoordsProvider getIcon();

    boolean canInsert();

    boolean canExtract();
}