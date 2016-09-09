package com.feed_the_beast.ftbl.api.item;

import java.util.Collection;

/**
 * Created by LatvianModder on 09.08.2016.
 */
public interface IMetaLookup<T extends IMaterial>
{
    Collection<T> getValues();

    T get(int metadata);
}