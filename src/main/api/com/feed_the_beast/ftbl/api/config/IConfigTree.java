package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.lib.config.PropertyNull;
import com.feed_the_beast.ftbl.lib.io.IExtendedIOObject;
import net.minecraft.util.IJsonSerializable;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * @author LatvianModder
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

    @Nullable
    default IConfigKey getKey(String id)
    {
        for(IConfigKey key : getTree().keySet())
        {
            if(key.getName().equalsIgnoreCase(id))
            {
                return key;
            }
        }

        return null;
    }
}