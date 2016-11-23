package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface ISharedData
{
    Side getSide();

    IPackMode getPackMode();

    UUID getUniverseID();

    boolean hasOptionalServerMod(@Nullable String id);
}