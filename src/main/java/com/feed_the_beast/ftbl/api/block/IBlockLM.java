package com.feed_the_beast.ftbl.api.block;

import com.feed_the_beast.ftbl.api.item.IItemLM;
import net.minecraft.item.ItemBlock;

public interface IBlockLM extends IItemLM
{
    ItemBlock createItemBlock();
    void loadTiles();
}