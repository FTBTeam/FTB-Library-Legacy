package com.feed_the_beast.ftbl.api_impl;

import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Created by LatvianModder on 09.02.2016.
 */
public class ForgePlayerTemp extends ForgePlayer
{
    public final EntityPlayerMP player;

    public ForgePlayerTemp(EntityPlayerMP ep)
    {
        super(ep.getGameProfile());
        player = ep;
    }

    @Override
    public EntityPlayerMP getPlayer()
    {
        return player;
    }
}