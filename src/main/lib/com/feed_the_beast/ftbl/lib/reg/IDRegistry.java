package com.feed_the_beast.ftbl.lib.reg;

import gnu.trove.map.hash.TShortObjectHashMap;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public abstract class IDRegistry<K>
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
        if(IDToKey.containsValue(key))
        {
            return KeyToID.get(key);
        }

        short i = (short) (IDToKey.size() + 1);
        KeyToID.put(key, i);
        IDToKey.put(i, key);
        return i;
    }

    public abstract String createStringFromKey(K k);

    public abstract K createKeyFromString(String s);

    public TShortObjectHashMap<String> serialize()
    {
        TShortObjectHashMap<String> map = new TShortObjectHashMap<>();

        KeyToID.forEach((key, value) ->
        {
            map.put(value, createStringFromKey(key));
        });

        return map;
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
}