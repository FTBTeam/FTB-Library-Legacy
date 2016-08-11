package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.io.ByteIOStream;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ConfigEntryBool extends ConfigEntry implements IClickable
{
    public boolean defValue;
    private boolean value;

    public ConfigEntryBool(boolean def)
    {
        defValue = def;
        value = def;
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.BOOLEAN;
    }

    @Override
    public int getColor()
    {
        return getAsBoolean() ? 0x33AA33 : 0xD52834;
    }

    public void set(boolean v)
    {
        value = v;
    }

    @Override
    public void fromJson(@Nonnull JsonElement o)
    {
        set(o.getAsBoolean());
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsBoolean());
    }

    @Override
    public void onClicked(@Nonnull MouseButton button)
    {
        set(!getAsBoolean());
    }

    @Override
    public String getAsString()
    {
        return getAsBoolean() ? "true" : "false";
    }

    @Override
    public boolean getAsBoolean()
    {
        return value;
    }

    @Override
    public int getAsInt()
    {
        return getAsBoolean() ? 1 : 0;
    }

    @Override
    public double getAsDouble()
    {
        return getAsBoolean() ? 1D : 0D;
    }

    @Override
    public String getDefValueString()
    {
        return defValue ? "true" : "false";
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryBool entry = new ConfigEntryBool(defValue);
        entry.set(getAsBoolean());
        return entry;
    }

    @Override
    public void writeData(ByteIOStream io, boolean extended)
    {
        super.writeData(io, extended);
        io.writeByte((getAsBoolean() ? 1 : 0) | ((defValue ? 1 : 0) << 1));
    }

    @Override
    public void readData(ByteIOStream io, boolean extended)
    {
        super.readData(io, extended);

        byte i = io.readByte();

        set((i & 1) != 0);

        if(extended)
        {
            defValue = (i & 2) != 0;
        }
    }

    @Override
    public List<String> getVariants()
    {
        return Arrays.asList("true", "false");
    }
}