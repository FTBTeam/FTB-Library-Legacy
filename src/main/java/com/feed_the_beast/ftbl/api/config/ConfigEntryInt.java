package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.annotations.INumberBoundsContainer;
import com.latmod.lib.math.MathHelperLM;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;

public class ConfigEntryInt extends ConfigEntry implements INumberBoundsContainer
{
    public int defValue;
    private int value;
    private Integer minValue, maxValue;

    public ConfigEntryInt(int def)
    {
        defValue = def;
        set(def);
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.INT;
    }

    @Override
    public int getColor()
    {
        return 0xAA5AE8;
    }

    @Override
    public void setBounds(double min, double max)
    {
        minValue = min == Double.NEGATIVE_INFINITY ? null : (int) min;
        maxValue = max == Double.POSITIVE_INFINITY ? null : (int) max;
    }

    @Override
    public double getMin()
    {
        return minValue == null ? Double.NEGATIVE_INFINITY : minValue.doubleValue();
    }

    @Override
    public double getMax()
    {
        return maxValue == null ? Double.POSITIVE_INFINITY : maxValue.doubleValue();
    }

    public void set(int v)
    {
        value = MathHelperLM.clampInt(v, (int) getMin(), (int) getMax());
    }

    public void add(int i)
    {
        set(getAsInt() + i);
    }

    @Override
    public final void fromJson(@Nonnull JsonElement o)
    {
        set((o == null || o.isJsonNull()) ? defValue : o.getAsInt());
    }

    @Nonnull
    @Override
    public final JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsInt());
    }

    @Override
    public String getAsString()
    {
        return Integer.toString(getAsInt());
    }

    @Override
    public boolean getAsBoolean()
    {
        return getAsInt() != 0;
    }

    @Override
    public int getAsInt()
    {
        return value;
    }

    @Override
    public double getAsDouble()
    {
        return getAsInt();
    }

    @Override
    public String getDefValueString()
    {
        return Integer.toString(defValue);
    }

    @Override
    public String getMinValueString()
    {
        double d = getMin();

        if(d != Double.NEGATIVE_INFINITY)
        {
            return Integer.toString((int) d);
        }

        return null;
    }

    @Override
    public String getMaxValueString()
    {
        double d = getMax();

        if(d != Double.POSITIVE_INFINITY)
        {
            return Integer.toString((int) d);
        }

        return null;
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryInt entry = new ConfigEntryInt(defValue);
        entry.set(getAsInt());
        return entry;
    }

    @Override
    public void writeData(ByteBuf io, boolean extended)
    {
        super.writeData(io, extended);
        io.writeInt(getAsInt());

        if(extended)
        {
            io.writeInt(defValue);
            io.writeDouble(getMin());
            io.writeDouble(getMax());
        }
    }

    @Override
    public void readData(ByteBuf io, boolean extended)
    {
        super.readData(io, extended);
        set(io.readInt());

        if(extended)
        {
            defValue = io.readInt();
            double min = io.readDouble();
            double max = io.readDouble();
            setBounds(min, max);
        }
    }
}