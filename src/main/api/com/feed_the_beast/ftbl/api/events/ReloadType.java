package com.feed_the_beast.ftbl.api.events;

import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 24.04.2016.
 */
public enum ReloadType
{
    LOGIN(Side.CLIENT),
    SERVER_ONLY(Side.SERVER),
    CLIENT_ONLY(Side.CLIENT),
    SERVER_AND_CLIENT(null),
    SERVER_ONLY_NOTIFY_CLIENT(Side.SERVER);

    private final Side side;

    ReloadType(@Nullable Side s)
    {
        side = s;
    }

    public boolean reload(Side s)
    {
        return side == null || side == s;
    }
}