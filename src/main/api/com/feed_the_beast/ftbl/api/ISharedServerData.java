package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by LatvianModder on 12.11.2016.
 */
public interface ISharedServerData extends ISharedData
{
    @Override
    default Side getSide()
    {
        return Side.SERVER;
    }
}