package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.lib.config.PropertyNull;
import com.feed_the_beast.ftbl.lib.io.IExtendedIOObject;
import net.minecraft.util.IJsonSerializable;

import java.util.Map;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IConfigTree extends IExtendedIOObject, IJsonSerializable
{
    Map<IConfigKey, IConfigValue> getTree();

    default void add(IConfigKey key, IConfigValue value)
    {
        getTree().put(key, value);
    }

    default boolean has(IConfigKey key)
    {
        return getTree().containsKey(key);
    }

    default void remove(IConfigKey key)
    {
        getTree().remove(key);
    }

    default IConfigValue get(IConfigKey key)
    {
        IConfigValue v = getTree().get(key);
        return (v == null) ? PropertyNull.INSTANCE : v;
    }

    default boolean isEmpty()
    {
        return getTree().isEmpty();
    }

    IConfigTree copy();
}