package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.IIntIDRegistry;
import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public class IntIDRegistry implements INBTSerializable<NBTTagCompound>, IIntIDRegistry
{
    private final TIntObjectHashMap<ResourceLocation> IDToRL = new TIntObjectHashMap<>();
    private final Map<ResourceLocation, Integer> RLToID = new HashMap<>();

    @Nullable
    @Override
    public ResourceLocation getKeyFromID(int numID)
    {
        return IDToRL.get(numID);
    }

    @Override
    public int getIDFromKey(ResourceLocation key)
    {
        return IDToRL.containsValue(key) ? RLToID.get(key) : 0;
    }

    @Override
    public int getOrCreateIDFromKey(ResourceLocation key)
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

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        for(Map.Entry<ResourceLocation, Integer> entry : RLToID.entrySet())
        {
            nbt.setInteger(entry.getKey().toString(), entry.getValue());
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
            ResourceLocation key = new ResourceLocation(s);
            int id = nbt.getInteger(s);
            IDToRL.put(id, key);
            RLToID.put(key, id);
        }
    }
}