package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IIntIDRegistry;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public abstract class AbstractIntIDRegistry<K> implements IIntIDRegistry<K>, INBTSerializable<NBTTagCompound>
{
    private final TIntObjectHashMap<K> IDToRL = new TIntObjectHashMap<>();
    private final Map<K, Integer> RLToID = new HashMap<>();

    @Nullable
    @Override
    public K getKeyFromID(int numID)
    {
        return IDToRL.get(numID);
    }

    @Override
    public int getIDFromKey(K key)
    {
        return IDToRL.containsValue(key) ? RLToID.get(key) : 0;
    }

    @Override
    public int getOrCreateIDFromKey(K key)
    {
        if(IDToRL.containsValue(key))
        {
            return RLToID.get(key);
        }

        int i = 0;

        if(!IDToRL.isEmpty())
        {
            for(int i1 : IDToRL.keys())
            {
                if(i1 > i)
                {
                    i = i1;
                }
            }
        }

        i++;
        RLToID.put(key, i);
        IDToRL.put(i, key);
        return i;
    }

    public abstract String createFromKey(K k);

    public abstract K createFromString(String s);

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        for(Map.Entry<K, Integer> entry : RLToID.entrySet())
        {
            nbt.setInteger(createFromKey(entry.getKey()), entry.getValue());
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        IDToRL.clear();
        RLToID.clear();

        for(String s : nbt.getKeySet())
        {
            K key = createFromString(s);
            int id = nbt.getInteger(s);
            IDToRL.put(id, key);
            RLToID.put(key, id);
        }
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