package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 18.05.2016.
 */
public abstract class ForgePlayerEvent extends Event
{
    private final IForgePlayer player;

    public ForgePlayerEvent(IForgePlayer p)
    {
        player = p;
    }

    public IForgePlayer getPlayer()
    {
        return player;
    }
}