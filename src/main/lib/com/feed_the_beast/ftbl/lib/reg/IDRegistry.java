package com.feed_the_beast.ftbl.lib.reg;

import gnu.trove.map.hash.TShortObjectHashMap;
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
    private final TShortObjectHashMap<K> IDToKey = new TShortObjectHashMap<>();
    private final Map<K, Short> KeyToID = new HashMap<>();

    @Nullable
    public K getKeyFromID(short numID)
    {
        return IDToKey.get(numID);
    }

    public short getIDFromKey(K key)
    {
        return IDToKey.containsValue(key) ? KeyToID.get(key) : 0;
    }

    public void generateIDs(Collection<K> keys)
    {
        IDToKey.clear();
        KeyToID.clear();

        keys.forEach(key ->
        {
            short i = (short) (IDToKey.size() + 1);
            KeyToID.put(key, i);
            IDToKey.put(i, key);
        });
    }

    public short generateID(K key)
    {
        short i = getIDFromKey(key);

        if(i == 0)
        {
            i = (short) (IDToKey.size() + 1);
            KeyToID.put(key, i);
            IDToKey.put(i, key);
        }

        return i;
    }

    public abstract String createStringFromKey(K k);

    public abstract K createKeyFromString(String s);

    public void serialize(TShortObjectHashMap<String> map)
    {
        KeyToID.forEach((key, value) -> map.put(value, createStringFromKey(key)));
    }

    public void deserialize(TShortObjectHashMap<String> map)
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
        KeyToID.forEach((key, value) -> nbt.setShort(createStringFromKey(key), value));
        return nbt;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt)
    {
        IDToKey.clear();
        KeyToID.clear();

        for(String key : nbt.getKeySet())
        {
            short val = nbt.getShort(key);
            K key1 = createKeyFromString(key);
            IDToKey.put(val, key1);
            KeyToID.put(key1, val);
        }
    }
}