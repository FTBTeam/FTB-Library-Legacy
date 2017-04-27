package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface ISharedData
{
    Side getSide();

    IPackMode getPackMode();

    UUID getUniverseID();

    Collection<String> optionalServerMods();
}