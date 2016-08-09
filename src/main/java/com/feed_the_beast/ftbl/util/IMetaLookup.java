package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.api.item.IMaterial;

import java.util.Collection;

/**
 * Created by LatvianModder on 09.08.2016.
 */
public interface IMetaLookup<T extends IMaterial>
{
    Collection<T> getValues();

    T getDefaultValue();

    T get(int metadata);
}