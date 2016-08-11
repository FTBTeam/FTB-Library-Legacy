package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.google.common.base.Preconditions;
import net.minecraftforge.event.AttachCapabilitiesEvent;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class AttachTeamCapabilitiesEvent extends AttachCapabilitiesEvent
{
    private final IForgeTeam team;

    public AttachTeamCapabilitiesEvent(IForgeTeam t)
    {
        super(t);
        team = Preconditions.checkNotNull(t, "Null IForgeTeam in AttachTeamCapabilitiesEvent!");
    }

    @Nonnull
    public IForgeTeam getTeam()
    {
        return team;
    }
}