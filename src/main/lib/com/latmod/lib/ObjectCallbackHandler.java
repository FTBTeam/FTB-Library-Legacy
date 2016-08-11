package com.latmod.lib;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 30.05.2016.
 */
public interface ObjectCallbackHandler
{
    void onCallback(@Nullable Object id, @Nullable Object val);
}