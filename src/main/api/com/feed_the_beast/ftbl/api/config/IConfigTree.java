package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.config.impl.PropertyNull;
import com.latmod.lib.annotations.Flags;
import com.latmod.lib.annotations.Info;
import com.latmod.lib.io.Bits;
import com.latmod.lib.io.IExtendedIOObject;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Locale;
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

    default void add(IConfigKey key)
    {
        add(key, key.getDefValue().copy());
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

    default void addAll(String id, Class<?> clazz, @Nullable Object obj)
    {
        try
        {
            for(Field field : clazz.getDeclaredFields())
            {
                if(IConfigValue.class.isAssignableFrom(field.getType()))
                {
                    if(field.isAnnotationPresent(Flags.class))
                    {
                        if(Bits.getFlag(field.getAnnotation(Flags.class).value(), Flags.EXCLUDED))
                        {
                            continue;
                        }
                    }

                    IConfigValue value = (IConfigValue) field.get(obj);
                    ConfigKey key = new ConfigKey(id + '.' + field.getName().toLowerCase(Locale.ENGLISH), value.copy(), null);

                    if(field.isAnnotationPresent(Info.class))
                    {
                        key.setInfo(field.getAnnotation(Info.class).value());
                    }

                    add(key, value);
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}