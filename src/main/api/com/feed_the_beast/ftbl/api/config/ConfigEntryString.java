package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.io.ByteIOStream;

import javax.annotation.Nonnull;

public class ConfigEntryString extends ConfigEntry
{
    public String defValue;
    private String value;

    public ConfigEntryString(String def)
    {
        set(def);
        defValue = def == null ? "" : def;
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
        value = v == null ? "" : v;
    }

    @Override
    public final void fromJson(@Nonnull JsonElement o)
    {
        set(o.getAsString());
    }

    @Nonnull
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
}