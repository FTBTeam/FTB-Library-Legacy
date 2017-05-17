package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;

/**
 * @author LatvianModder
 */
public class ForgePlayerLoggedInEvent extends ForgePlayerEvent
{
    private final boolean first;

    public ForgePlayerLoggedInEvent(IForgePlayer player, boolean f)
    {
        super(player);
        first = f;
    }

    public boolean isFirstLogin()
    {
        return first;
    }
}