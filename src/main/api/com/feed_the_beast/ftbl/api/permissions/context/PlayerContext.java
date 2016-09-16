package com.feed_the_beast.ftbl.api.permissions.context;

import com.google.common.base.Preconditions;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class PlayerContext extends Context
{
    private final EntityPlayer player;

    public PlayerContext(EntityPlayer ep)
    {
        player = Preconditions.checkNotNull(ep, "Player can't be null in PlayerContext!");
    }

    @Override
    public World getWorld()
    {
        return player.getEntityWorld();
    }

    @Override
    public EntityPlayer getPlayer()
    {
        return player;
    }
}