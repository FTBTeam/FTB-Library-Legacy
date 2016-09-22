package com.feed_the_beast.ftbl.api.tile;

import com.latmod.lib.ILangKeyContainer;
import com.latmod.lib.client.ITextureCoordsProvider;
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