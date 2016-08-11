package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgeTeamSettingsEvent extends ForgeTeamEvent
{
    private final ConfigGroup settings;

    public ForgeTeamSettingsEvent(IForgeTeam p, ConfigGroup g)
    {
        super(p);
        settings = g;
    }

    public ConfigGroup getSettings()
    {
        return settings;
    }
}