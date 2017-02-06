package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 24.04.2016.
 */
public enum EnumReloadType
{
    SERVER_STARTED(Side.SERVER, false),
    LOGIN(Side.CLIENT, false),
    MODE_CHANGED(null, true),
    SERVER_COMMAND(Side.SERVER, true),
    CLIENT_COMMAND(Side.CLIENT, true);

    private final Side side;
    private final boolean command;

    EnumReloadType(@Nullable Side s, boolean b)
    {
        side = s;
        command = b;
    }

    public boolean command()
    {
        return command;
    }

    public boolean reload(Side s)
    {
        return side == null || side == s;
    }
}