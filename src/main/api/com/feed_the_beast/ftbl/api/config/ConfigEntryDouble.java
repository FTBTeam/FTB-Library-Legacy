package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.annotations.INumberBoundsContainer;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.math.MathHelperLM;
import com.latmod.lib.util.LMStringUtils;

import javax.annotation.Nonnull;

public class ConfigEntryDouble extends ConfigEntry implements INumberBoundsContainer
{
    public double defValue;
    private double value;
    private double minValue, maxValue;

    public ConfigEntryDouble(double d)
    {
        defValue = d;
        minValue = Double.NEGATIVE_INFINITY;
        maxValue = Double.POSITIVE_INFINITY;
        set(d);
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.DOUBLE;
    }

    @Override
    public int getColor()
    {
        return 0xAA5AE8;
    }

    @Override
    public void setBounds(double min, double max)
    {
        minValue = min;
        maxValue = max;
    }

    @Override
    public double getMin()
    {
        return minValue;
    }

    @Override
    public double getMax()
    {
        return maxValue;
    }

    public void set(double v)
    {
        value = MathHelperLM.clamp(v, getMin(), getMax());
    }

    public void add(double v)
    {
        set(getAsDouble() + v);
    }

    @Override
    public final void fromJson(@Nonnull JsonElement o)
    {
        set(o.getAsDouble());
    }

    @Nonnull
    @Override
    public final JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsDouble());
    }

    @Override
    public String getAsString()
    {
        return Double.toString(getAsDouble());
    }

    @Override
    public int getAsInt()
    {
        return (int) getAsDouble();
    }

    @Override
    public double getAsDouble()
    {
        return value;
    }

    @Override
    public String getDefValueString()
    {
        return Double.toString(defValue);
    }

    @Override
    public String getMinValueString()
    {
        double d = getMin();

        if(d != Double.NEGATIVE_INFINITY)
        {
            return LMStringUtils.formatDouble(d);
        }

        return null;
    }

    @Override
    public String getMaxValueString()
    {
        double d = getMax();

        if(d != Double.POSITIVE_INFINITY)
        {
            return LMStringUtils.formatDouble(d);
        }

        return null;
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryDouble entry = new ConfigEntryDouble(defValue);
        entry.set(getAsDouble());
        return entry;
    }

    @Override
    public void writeData(ByteIOStream io, boolean extended)
    {
        super.writeData(io, extended);
        io.writeDouble(getAsDouble());

        if(extended)
        {
            io.writeDouble(defValue);
            io.writeDouble(getMin());
            io.writeDouble(getMax());
        }
    }

    @Override
    public void readData(ByteIOStream io, boolean extended)
    {
        super.readData(io, extended);
        set(io.readDouble());

        if(extended)
        {
            defValue = io.readDouble();
            double min = io.readDouble();
            double max = io.readDouble();
            setBounds(min, max);
        }
    }
}