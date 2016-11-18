package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 24.04.2016.
 */
public enum EnumReloadType
{
    LOGIN(Side.CLIENT),
    SERVER_ONLY(Side.SERVER),
    CLIENT_ONLY(Side.CLIENT),
    SERVER_AND_CLIENT(null),
    SERVER_ONLY_NOTIFY_CLIENT(Side.SERVER);

    private final Side side;

    EnumReloadType(@Nullable Side s)
    {
        side = s;
    }

    public boolean reload(Side s)
    {
        return side == null || side == s;
    }
}