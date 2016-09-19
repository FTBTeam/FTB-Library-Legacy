package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api_impl.config.PropertyNull;
import com.latmod.lib.io.IExtendedIOObject;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public interface IConfigTree extends IExtendedIOObject, IJsonSerializable, INBTSerializable<NBTBase>
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
}