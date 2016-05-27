package com.feed_the_beast.ftbl.api.tile;

import com.feed_the_beast.ftbl.api.ForgePlayerMP;

public interface ISecureTile
{
    boolean canPlayerInteract(ForgePlayerMP player, boolean breakBlock);

    void onPlayerNotOwner(ForgePlayerMP player, boolean breakBlock);
}