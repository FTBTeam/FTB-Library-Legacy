package com.feed_the_beast.ftbl.util;

import com.feed_the_beast.ftbl.api.item.IMaterial;

import javax.annotation.Nonnull;
import java.util.Collection;

/**
 * Created by LatvianModder on 09.08.2016.
 */
public interface IMetaLookup<T extends IMaterial>
{
    @Nonnull
    Collection<T> getValues();

    @Nonnull
    T get(int metadata);
}