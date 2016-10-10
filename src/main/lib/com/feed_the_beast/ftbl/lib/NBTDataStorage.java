package com.feed_the_beast.ftbl.lib;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 10.10.2016.
 */
public class NBTDataStorage implements INBTSerializable<NBTTagCompound>
{
    private final Map<String, INBTData> map;

    public NBTDataStorage()
    {
        map = new HashMap<>();
    }

    public void add(INBTData data)
    {
        map.put(data.getName(), data);
    }

    public INBTData get(String id)
    {
        return map.get(id);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        for(INBTData data : map.values())
        {
            tag.setTag(data.getName(), data.serializeNBT());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        for(String s : nbt.getKeySet())
        {
            INBTData data = map.get(s);

            if(data != null)
            {
                data.deserializeNBT(nbt.getCompoundTag(s));
            }
        }
    }
}