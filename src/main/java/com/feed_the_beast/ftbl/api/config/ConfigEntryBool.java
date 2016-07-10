package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;

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
    public void writeData(ByteBuf io, boolean extended)
    {
        super.writeData(io, extended);
        io.writeBoolean(getAsBoolean());

        if(extended)
        {
            io.writeBoolean(defValue);
        }
    }

    @Override
    public void readData(ByteBuf io, boolean extended)
    {
        super.readData(io, extended);

        set(io.readBoolean());

        if(extended)
        {
            defValue = io.readBoolean();
        }
    }

    @Override
    public List<String> getVariants()
    {
        return Arrays.asList("true", "false");
    }
}