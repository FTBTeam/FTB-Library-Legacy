package com.latmod.lib;

import javax.annotation.Nonnull;

/**
 * Created by LatvianModder on 03.01.2016.
 */
public interface RemoveFilter<E>
{
    boolean remove(@Nonnull E e);
}