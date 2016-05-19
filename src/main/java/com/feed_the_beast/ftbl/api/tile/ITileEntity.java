package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.util.BlockDimPos;
import net.minecraft.tileentity.TileEntity;

/**
 * Created by LatvianModder on 12.03.2016.
 */
public interface ITileEntity
{
    TileEntity getTile();

    BlockDimPos getDimPos();
}
