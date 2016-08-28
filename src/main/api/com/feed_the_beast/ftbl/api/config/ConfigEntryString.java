package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.io.ByteIOStream;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;

public class ConfigEntryString extends ConfigEntry implements INBTSerializable<NBTTagString>
{
    public String defValue;
    private String value;

    public ConfigEntryString(String def)
    {
        defValue = value = def;
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.STRING;
    }

    @Override
    public int getColor()
    {
        return 0xFFAA49;
    }

    public void set(String v)
    {
        value = v;
    }

    @Override
    public final void fromJson(JsonElement o)
    {
        set(o.getAsString());
    }

    @Override
    public final JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsString());
    }

    @Override
    public String getAsString()
    {
        return value;
    }

    @Override
    public boolean getAsBoolean()
    {
        return getAsString().equals("true");
    }

    @Override
    public int getAsInt()
    {
        return Integer.parseInt(getAsString());
    }

    @Override
    public double getAsDouble()
    {
        return Double.parseDouble(getAsString());
    }

    @Override
    public String getDefValueString()
    {
        return defValue;
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryString entry = new ConfigEntryString(defValue);
        entry.set(getAsString());
        return entry;
    }

    @Override
    public void writeData(ByteIOStream io, boolean extended)
    {
        super.writeData(io, extended);
        io.writeUTF(getAsString());

        if(extended)
        {
            io.writeUTF(defValue);
        }
    }

    @Override
    public void readData(ByteIOStream io, boolean extended)
    {
        super.readData(io, extended);
        set(io.readUTF());

        if(extended)
        {
            defValue = io.readUTF();
        }
    }

    @Override
    public NBTTagString serializeNBT()
    {
        return new NBTTagString(getAsString());
    }

    @Override
    public void deserializeNBT(NBTTagString nbt)
    {
        set(nbt.getString());
    }
}