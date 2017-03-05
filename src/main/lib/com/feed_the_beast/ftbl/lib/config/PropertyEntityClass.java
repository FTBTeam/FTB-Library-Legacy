package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 24.09.2016.
 */
public class PropertyEntityClass extends PropertyBase
{
    public static final String ID = "entity_class_list";

    private Class<? extends Entity> entityClass;

    public PropertyEntityClass()
    {
    }

    public PropertyEntityClass(@Nullable Class<? extends Entity> c)
    {
        entityClass = c;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Nullable
    public Class<? extends Entity> getEntityClass()
    {
        return entityClass;
    }

    public void setEntityClass(@Nullable Class<? extends Entity> c)
    {
        entityClass = c;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getEntityClass();
    }

    public boolean matches(Class<?> c, boolean assignable)
    {
        Class<? extends Entity> c0 = getEntityClass();
        return c0 != null && (assignable ? c0.isAssignableFrom(c) : c0.equals(c));
    }

    @Override
    public String getString()
    {
        Class<?> c = getEntityClass();
        String s = c == null ? "" : EntityList.CLASS_TO_NAME.get(c);
        return s == null ? "" : s;
    }

    @Override
    public boolean setValueFromString(String text, boolean simulate)
    {
        Class<? extends Entity> c = EntityList.NAME_TO_CLASS.get(text);

        if(c != null)
        {
            if(!simulate)
            {
                setEntityClass(c);
            }

            return true;
        }

        return false;
    }

    @Override
    public boolean getBoolean()
    {
        return getEntityClass() != null;
    }

    @Override
    public int getInt()
    {
        return getBoolean() ? 1 : 0;
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyEntityClass(getEntityClass());
    }

    @Override
    public void fromJson(JsonElement o)
    {
        setValueFromString(o.getAsString(), false);
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getString());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        ByteBufUtils.writeUTF8String(data, getString());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setValueFromString(ByteBufUtils.readUTF8String(data), false);
    }
}