package com.feed_the_beast.ftbl.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 10.10.2016.
 */
public class NBTDataStorage implements INBTSerializable<NBTTagCompound>
{
    private final Map<ResourceLocation, INBTData> map;

    public NBTDataStorage()
    {
        map = new HashMap<>();
    }

    public void add(INBTData data)
    {
        map.put(data.getID(), data);
    }

    public INBTData get(ResourceLocation id)
    {
        return map.get(id);
    }

    public boolean isEmpty()
    {
        return map.isEmpty();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();

        for(INBTData data : map.values())
        {
            NBTTagCompound nbt1 = new NBTTagCompound();
            data.writeData(nbt1);

            if(!nbt1.hasNoTags())
            {
                nbt.setTag(data.getID().toString(), nbt1);
            }
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        for(INBTData data : map.values())
        {
            data.readData(nbt.getCompoundTag(data.getID().toString()));
        }
    }
}