package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyByte extends PropertyInt
{
    public static final String ID = "byte";

    public PropertyByte()
    {
    }

    public PropertyByte(int v)
    {
        super(v);
    }

    public PropertyByte(int v, int min, int max)
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
        return new PropertyByte(getInt(), getMin(), getMax());
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagByte((byte) getInt());
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
        data.writeByte(getInt());
        data.writeByte(getMin());
        data.writeByte(getMax());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setInt(data.readByte());
        setMin(data.readByte());
        setMax(data.readByte());
    }
}