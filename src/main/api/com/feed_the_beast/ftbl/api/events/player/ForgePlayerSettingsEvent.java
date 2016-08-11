package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgePlayerSettingsEvent extends ForgePlayerEvent
{
    private final ConfigGroup settings;

    public ForgePlayerSettingsEvent(IForgePlayer p, ConfigGroup g)
    {
        super(p);
        settings = g;
    }

    public ConfigGroup getSettings()
    {
        return settings;
    }
}