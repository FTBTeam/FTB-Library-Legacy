package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.ISyncedRegistry;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 16.08.2016.
 */
public class SyncedRegistry<K, V> extends SimpleRegistry<K, V> implements ISyncedRegistry<K, V>, INBTSerializable<NBTTagCompound>
{
    private final AbstractIntIDRegistry<K> intIDs;

    public SyncedRegistry(AbstractIntIDRegistry<K> r, boolean overrides)
    {
        super(overrides);
        intIDs = r;
    }

    @Override
    public V register(K key, V v)
    {
        V v1 = super.register(key, v);

        if(v1 == v)
        {
            getOrCreateIDFromKey(key);
        }

        return v1;
    }

    @Nullable
    @Override
    public K getKeyFromID(int numID)
    {
        return intIDs.getKeyFromID(numID);
    }

    @Override
    public int getIDFromKey(K key)
    {
        return intIDs.getIDFromKey(key);
    }

    @Override
    public int getOrCreateIDFromKey(K key)
    {
        return intIDs.getOrCreateIDFromKey(key);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        return intIDs.serializeNBT();
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        intIDs.deserializeNBT(nbt);
    }

    @Override
    public NBTTagCompound writeSyncData(EntityPlayerMP player, IForgePlayer forgePlayer)
    {
        return serializeNBT();
    }

    @Override
    public void readSyncData(NBTTagCompound nbt)
    {
        deserializeNBT(nbt);
    }
}