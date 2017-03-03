package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import com.feed_the_beast.ftbl.lib.util.LMStringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyDouble extends PropertyBase
{
    public static final String ID = "double";

    private double value;
    private double min = Double.NEGATIVE_INFINITY;
    private double max = Double.POSITIVE_INFINITY;

    public PropertyDouble()
    {
    }

    public PropertyDouble(double v)
    {
        value = v;
    }

    public PropertyDouble(double v, double mn, double mx)
    {
        value = v;
        min = mn;
        max = mx;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Override
    public double getDouble()
    {
        return value;
    }

    public void setDouble(double v)
    {
        value = v;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getDouble();
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

    public double getMin()
    {
        return min;
    }

    public double getMax()
    {
        return max;
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
    public boolean equalsValue(IConfigValue value)
    {
        return getDouble() == value.getDouble();
    }

    @Override
    public int getColor()
    {
        return 0xAA5AE8;
    }

    @Override
    public void addInfo(IConfigKey key, List<String> list)
    {
        super.addInfo(key, list);

        double m = getMin();

        if(m != Double.NEGATIVE_INFINITY)
        {
            list.add(TextFormatting.AQUA + "Min: " + LMStringUtils.formatDouble(m));
        }

        m = getMax();

        if(m != Double.POSITIVE_INFINITY)
        {
            list.add(TextFormatting.AQUA + "Max: " + LMStringUtils.formatDouble(m));
        }
    }

    @Override
    public boolean setValueFromString(String text, boolean simulate)
    {
        if(MathHelperLM.canParseDouble(text))
        {
            if(!simulate)
            {
                setDouble(Double.parseDouble(text));
            }

            return true;
        }

        return false;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagDouble(getDouble());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        setDouble(((NBTPrimitive) nbt).getDouble());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setDouble(json.getAsDouble());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getDouble());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        data.writeDouble(getDouble());
        data.writeDouble(getMin());
        data.writeDouble(getMax());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setDouble(data.readDouble());
        setMin(data.readDouble());
        setMax(data.readDouble());
    }
}