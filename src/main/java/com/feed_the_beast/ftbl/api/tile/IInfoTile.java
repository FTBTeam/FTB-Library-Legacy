package com.feed_the_beast.ftbl.api.tile;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Created by LatvianModder on 20.05.2016.
 */
public interface IInfoTile
{
    interface Stack
    {
        ItemStack getInfoItem(TileInfoDataAccessor info);
    }

    void getInfo(TileInfoDataAccessor info, List<String> list, boolean adv);
}