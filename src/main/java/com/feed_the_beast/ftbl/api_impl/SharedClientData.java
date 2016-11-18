package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.ISharedClientData;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 12.11.2016.
 */
public class SharedClientData extends SharedData implements ISharedClientData
{
    public static final SharedClientData INSTANCE = new SharedClientData();

    public boolean hasServer, useFTBPrefix, isClientPlayerOP;

    private SharedClientData()
    {
    }

    @Override
    public void reset()
    {
        super.reset();
        optionalServerMods.clear();
        hasServer = false;
        useFTBPrefix = false;
        isClientPlayerOP = false;
    }

    @Override
    public boolean hasOptionalServerMod(@Nullable String id)
    {
        return (id == null || id.isEmpty()) ? hasServer : optionalServerMods.contains(id);
    }

    @Override
    public boolean useFTBPrefix()
    {
        return useFTBPrefix;
    }

    @Override
    public boolean isClientOP()
    {
        return isClientPlayerOP;
    }
}
