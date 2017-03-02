package com.feed_the_beast.ftbl.lib.reg;

import gnu.trove.map.hash.TIntObjectHashMap;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public abstract class IDRegistry<K> implements INBTSerializable<NBTTagCompound>
{
    private final TIntObjectHashMap<K> IDToKey = new TIntObjectHashMap<>();
    private final Map<K, Integer> KeyToID = new HashMap<>();

    public void clear()
    {
        if(!IDToKey.isEmpty())
        {
            IDToKey.clear();
            KeyToID.clear();
        }
    }

    @Nullable
    public K getKeyFromID(int numID)
    {
        return IDToKey.get(numID);
    }

    public int getIDFromKey(K key)
    {
        return IDToKey.containsValue(key) ? KeyToID.get(key) : 0;
    }

    public void generateIDs(Collection<K> keys)
    {
        clear();

        keys.forEach(key ->
        {
            int i = IDToKey.size() + 1;
            KeyToID.put(key, i);
            IDToKey.put(i, key);
        });
    }

    public int generateID(K key)
    {
        int i = getIDFromKey(key);

        if(i == 0)
        {
            i = IDToKey.size() + 1;
            KeyToID.put(key, i);
            IDToKey.put(i, key);
        }

        return i;
    }

    public abstract String createStringFromKey(K k);

    public abstract K createKeyFromString(String s);

    public void serialize(TIntObjectHashMap<String> map)
    {
        KeyToID.forEach((key, value) -> map.put(value, createStringFromKey(key)));
    }

    public void deserialize(TIntObjectHashMap<String> map)
    {
        map.forEachEntry((key, value) ->
        {
            K value1 = createKeyFromString(value);
            IDToKey.put(key, value1);
            KeyToID.put(value1, key);
            return true;
        });
    }

    public String toString()
    {
        return IDToKey.toString();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound nbt = new NBTTagCompound();
        KeyToID.forEach((key, value) -> nbt.setInteger(createStringFromKey(key), value));
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        clear();

        for(String key : nbt.getKeySet())
        {
            int val = nbt.getInteger(key);
            K key1 = createKeyFromString(key);
            IDToKey.put(val, key1);
            KeyToID.put(key1, val);
        }
    }
}