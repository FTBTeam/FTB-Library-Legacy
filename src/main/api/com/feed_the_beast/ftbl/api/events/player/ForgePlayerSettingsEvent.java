package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class ForgePlayerSettingsEvent extends ForgePlayerEvent
{
    private final IConfigTree settings;

    public ForgePlayerSettingsEvent(IForgePlayer player, IConfigTree tree)
    {
        super(player);
        settings = tree;
    }

    public IConfigTree getSettings()
    {
        return settings;
    }
}