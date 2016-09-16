package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgePlayerSettingsEvent extends ForgePlayerEvent
{
    private final IConfigTree settings;

    public ForgePlayerSettingsEvent(IForgePlayer p, IConfigTree g)
    {
        super(p);
        settings = g;
    }

    public IConfigTree getSettings()
    {
        return settings;
    }
}