package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.LMColor;
import com.latmod.lib.io.ByteIOStream;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.common.util.INBTSerializable;

public class ConfigEntryColor extends ConfigEntry implements INBTSerializable<NBTTagInt>
{
    public final LMColor.RGB value;
    public final LMColor.RGB defValue;

    public ConfigEntryColor(LMColor def)
    {
        value = new LMColor.RGB();
        value.set(def);

        defValue = new LMColor.RGB();
        defValue.set(def);
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.COLOR;
    }

    @Override
    public int getColor()
    {
        return value.color();
    }

    @Override
    public void fromJson(JsonElement o)
    {
        value.setRGBA(o.getAsInt());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(value.color());
    }

    @Override
    public String getAsString()
    {
        return value.toString();
    }

    @Override
    public int getAsInt()
    {
        return value.color();
    }

    @Override
    public String getDefValueString()
    {
        return defValue.toString();
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryColor entry = new ConfigEntryColor(defValue);
        entry.value.set(value);
        return entry;
    }

    @Override
    public void writeData(ByteIOStream io, boolean extended)
    {
        super.writeData(io, extended);
        io.writeInt(value.color());

        if(extended)
        {
            io.writeInt(defValue.color());
        }
    }

    @Override
    public void readData(ByteIOStream io, boolean extended)
    {
        super.readData(io, extended);
        value.setRGBA(io.readInt());

        if(extended)
        {
            defValue.setRGBA(io.readInt());
        }
    }

    @Override
    public NBTTagInt serializeNBT()
    {
        return new NBTTagInt(value.color());
    }

    @Override
    public void deserializeNBT(NBTTagInt nbt)
    {
        value.setRGBA(nbt.getInt());
    }
}