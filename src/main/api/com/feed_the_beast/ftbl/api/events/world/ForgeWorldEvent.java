package com.feed_the_beast.ftbl.api.events.world;

import com.feed_the_beast.ftbl.api.IForgeWorld;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Created by LatvianModder on 18.05.2016.
 */
public abstract class ForgeWorldEvent extends Event
{
    private final IForgeWorld world;

    public ForgeWorldEvent(IForgeWorld w)
    {
        world = w;
    }

    public IForgeWorld getWorld()
    {
        return world;
    }
}