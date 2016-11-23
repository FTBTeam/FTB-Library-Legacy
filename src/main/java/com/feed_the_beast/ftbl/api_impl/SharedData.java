package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.IPackMode;
import com.feed_the_beast.ftbl.api.ISharedData;
import com.feed_the_beast.ftbl.api.NotificationID;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public abstract class SharedData implements ISharedData
{
    public IPackMode currentMode;
    public UUID universeID;
    public final Collection<String> optionalServerMods = new HashSet<>();
    public final Map<NotificationID, INotification> notifications = new HashMap<>();

    SharedData()
    {
    }

    public void reset()
    {
        currentMode = null;
        universeID = null;
    }

    @Override
    public IPackMode getPackMode()
    {
        if(currentMode == null)
        {
            currentMode = getSide().isClient() ? new PackMode("default") : FTBLibIntegrationInternal.API.getPackModes().getDefault();
        }

        return currentMode;
    }

    @Override
    public UUID getUniverseID()
    {
        if(universeID == null || (universeID.getLeastSignificantBits() == 0L && universeID.getMostSignificantBits() == 0L))
        {
            universeID = UUID.randomUUID();
        }

        return universeID;
    }
}