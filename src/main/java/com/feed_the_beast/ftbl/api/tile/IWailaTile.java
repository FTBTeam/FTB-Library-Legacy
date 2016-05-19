package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.waila.WailaDataAccessor;
import net.minecraft.item.ItemStack;

import java.util.List;

public interface IWailaTile extends ITileEntity
{
    interface Stack extends IWailaTile
    {
        ItemStack getWailaStack(WailaDataAccessor data);
    }

    interface Head extends IWailaTile
    {
        void addWailaHead(WailaDataAccessor data, List<String> info);
    }

    interface Body extends IWailaTile
    {
        void addWailaBody(WailaDataAccessor data, List<String> info);
    }

    interface Tail extends IWailaTile
    {
        void addWailaTail(WailaDataAccessor data, List<String> info);
    }
}