package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import net.minecraft.util.DamageSource;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgePlayerDeathEvent extends ForgePlayerEvent
{
    private final DamageSource damageSource;

    public ForgePlayerDeathEvent(IForgePlayer player, DamageSource ds)
    {
        super(player);
        damageSource = ds;
    }

    public DamageSource getDamageSource()
    {
        return damageSource;
    }
}