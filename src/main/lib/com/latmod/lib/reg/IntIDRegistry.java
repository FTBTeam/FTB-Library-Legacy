package com.latmod.lib.reg;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public abstract class IntIDRegistry<K>
{
    private final TIntObjectMap<K> IDToKey = new TIntObjectHashMap<>();
    private final Map<K, Integer> KeyToID = new HashMap<>();

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
        IDToKey.clear();
        KeyToID.clear();

        keys.forEach(key ->
        {
            int i = IDToKey.size() + 1;
            KeyToID.put(key, i);
            IDToKey.put(i, key);
        });
    }

    public int generateID(K key)
    {
        if(IDToKey.containsValue(key))
        {
            return KeyToID.get(key);
        }

        int i = IDToKey.size() + 1;
        KeyToID.put(key, i);
        IDToKey.put(i, key);
        return i;
    }

    public abstract String createStringFromKey(K k);

    public abstract K createKeyFromString(String s);

    public TIntObjectMap<String> serialize()
    {
        TIntObjectHashMap<String> map = new TIntObjectHashMap<>();

        KeyToID.forEach((key, value) ->
        {
            map.put(value, createStringFromKey(key));
        });

        return map;
    }

    public void deserialize(TIntObjectMap<String> map)
    {
        map.forEachEntry((key, value) ->
        {
            K value1 = createKeyFromString(value);
            IDToKey.put(key, value1);
            KeyToID.put(value1, key);
            return true;
        });
    }
}