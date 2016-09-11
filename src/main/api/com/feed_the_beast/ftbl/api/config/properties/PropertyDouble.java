package com.feed_the_beast.ftbl.api.config.properties;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.annotations.INumberBoundsContainer;
import com.latmod.lib.util.LMStringUtils;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyDouble implements IConfigValue, INumberBoundsContainer
{
    public enum Provider implements IConfigValueProvider
    {
        INSTANCE;

        public static final ResourceLocation ID = new ResourceLocation(FTBLibFinals.MOD_ID, "double");

        @Override
        public ResourceLocation getID()
        {
            return ID;
        }

        @Override
        public IConfigValue createDefault()
        {
            return new PropertyDouble(0D);
        }

        @Override
        public int getColor(IConfigValue value)
        {
            return 0xAA5AE8;
        }

        @Override
        @Nullable
        public String getMinValue(IConfigValue value)
        {
            double d = ((PropertyDouble) value).getMin();

            if(d != Double.NEGATIVE_INFINITY)
            {
                return LMStringUtils.formatDouble(d);
            }

            return null;
        }

        @Override
        @Nullable
        public String getMaxValue(IConfigValue value)
        {
            double d = ((PropertyDouble) value).getMax();

            if(d != Double.POSITIVE_INFINITY)
            {
                return LMStringUtils.formatDouble(d);
            }

            return null;
        }
    }

    private double value;
    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;

    public PropertyDouble(double v)
    {
        value = v;
    }

    @Override
    public IConfigValueProvider getProvider()
    {
        return Provider.INSTANCE;
    }

    public PropertyDouble setMin(double v)
    {
        min = v;
        return this;
    }

    public PropertyDouble setMax(double v)
    {
        max = v;
        return this;
    }

    public double getDouble()
    {
        return value;
    }

    @Override
    public void setBounds(double mn, double mx)
    {
        setMin(mn);
        setMax(mx);
    }

    @Override
    public double getMin()
    {
        return min;
    }

    @Override
    public double getMax()
    {
        return max;
    }

    public void set(double v)
    {
        value = v;
    }


    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        data.writeDouble(getDouble());

        if(extended)
        {
            data.writeDouble(getMin());
            data.writeDouble(getMax());
        }
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        set(data.readDouble());

        if(extended)
        {
            setMin(data.readDouble());
            setMax(data.readDouble());
        }
    }

    @Override
    public String getString()
    {
        return Double.toString(getDouble());
    }

    @Override
    public boolean getBoolean()
    {
        return getDouble() != 0D;
    }

    @Override
    public int getInt()
    {
        return (int) getDouble();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyDouble(getDouble());
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagDouble(getDouble());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        set(((NBTPrimitive) nbt).getDouble());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        set(json.getAsDouble());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getDouble());
    }
}