package com.feed_the_beast.ftbl.api;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 17.08.2016.
 */
public interface IIntIDRegistry<K> extends ISyncData
{
    @Nullable
    K getKeyFromID(int numID);

    int getIDFromKey(K key);

    int getOrCreateIDFromKey(K key);
}