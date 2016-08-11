package com.feed_the_beast.ftbl.api.events.world;

import com.feed_the_beast.ftbl.api.IForgeWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class AttachWorldCapabilitiesEvent extends AttachCapabilitiesEvent
{
    private final IForgeWorld world;

    public AttachWorldCapabilitiesEvent(IForgeWorld w)
    {
        super(w);
        world = w;
    }

    public IForgeWorld getWorld()
    {
        return world;
    }
}