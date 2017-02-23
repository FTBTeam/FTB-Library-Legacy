package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagShort;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyShort extends PropertyInt
{
    public static final String ID = "short";

    public PropertyShort()
    {
    }

    public PropertyShort(int v)
    {
        super(v);
    }

    public PropertyShort(int v, int min, int max)
    {
        super(v, min, max);
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyShort(getInt(), getMin(), getMax());
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagShort((short) getInt());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        setInt(((NBTPrimitive) nbt).getInt());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setInt(json.getAsInt());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getInt());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        data.writeShort(getInt());
        data.writeShort(getMin());
        data.writeShort(getMax());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setInt(data.readShort());
        setMin(data.readShort());
        setMax(data.readShort());
    }
}