package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;

import java.util.Collections;
import java.util.Map;

/**
 * Created by LatvianModder on 24.09.2016.
 */
public enum EmptyConfigTree implements IConfigTree
{
    INSTANCE;

    @Override
    public void writeData(ByteBuf data)
    {
    }

    @Override
    public void readData(ByteBuf data)
    {
    }

    @Override
    public Map<IConfigKey, IConfigValue> getTree()
    {
        return Collections.emptyMap();
    }

    @Override
    public void add(IConfigKey key, IConfigValue value)
    {
    }

    @Override
    public boolean has(IConfigKey key)
    {
        return false;
    }

    @Override
    public void remove(IConfigKey key)
    {
    }

    @Override
    public IConfigValue get(IConfigKey key)
    {
        return PropertyNull.INSTANCE;
    }

    @Override
    public boolean isEmpty()
    {
        return true;
    }

    @Override
    public IConfigTree copy()
    {
        return this;
    }

    @Override
    public void fromJson(JsonElement json)
    {
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return JsonNull.INSTANCE;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagByte((byte) 0);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
    }
}
