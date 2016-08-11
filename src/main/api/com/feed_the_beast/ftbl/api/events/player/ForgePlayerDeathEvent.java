package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgePlayerDeathEvent extends ForgePlayerEvent
{
    public ForgePlayerDeathEvent(IForgePlayer p)
    {
        super(p);
    }
}