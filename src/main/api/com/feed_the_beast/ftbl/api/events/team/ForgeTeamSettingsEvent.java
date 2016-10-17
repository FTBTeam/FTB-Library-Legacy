package com.feed_the_beast.ftbl.api.events.team;

import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.config.IConfigTree;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgeTeamSettingsEvent extends ForgeTeamEvent
{
    private final IConfigTree settings;

    public ForgeTeamSettingsEvent(IForgeTeam team, IConfigTree tree)
    {
        super(team);
        settings = tree;
    }

    public IConfigTree getSettings()
    {
        return settings;
    }
}