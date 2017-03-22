package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.math.MathHelperLM;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyInt extends PropertyBase
{
    public static final String ID = "int";

    private int value;
    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

    public PropertyInt()
    {
    }

    public PropertyInt(int v)
    {
        value = v;
    }

    public PropertyInt(int v, int min, int max)
    {
        this(v);
        minValue = min;
        maxValue = max;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getInt();
    }

    public PropertyInt setMin(int v)
    {
        minValue = v;
        return this;
    }

    public PropertyInt setMax(int v)
    {
        maxValue = v;
        return this;
    }

    public int getMin()
    {
        return minValue;
    }

    public int getMax()
    {
        return maxValue;
    }

    public void setInt(int v)
    {
        value = MathHelper.clamp(v, getMin(), getMax());
    }

    @Override
    public String getString()
    {
        return Integer.toString(getInt());
    }

    @Override
    public boolean getBoolean()
    {
        return getInt() != 0;
    }

    @Override
    public int getInt()
    {
        return value;
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyInt(getInt(), getMin(), getMax());
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return getInt() == value.getInt();
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

        int m = getMin();

        if(m != Integer.MIN_VALUE)
        {
            list.add(TextFormatting.AQUA + "Min: " + m);
        }

        m = getMax();

        if(m != Integer.MAX_VALUE)
        {
            list.add(TextFormatting.AQUA + "Max: " + m);
        }
    }

    @Override
    public boolean setValueFromString(String text, boolean simulate)
    {
        if(MathHelperLM.canParseInt(text))
        {
            if(!simulate)
            {
                setInt(Integer.parseInt(text));
            }

            return true;
        }

        return false;
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
        data.writeInt(getInt());
        data.writeInt(getMin());
        data.writeInt(getMax());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setInt(data.readInt());
        setMin(data.readInt());
        setMax(data.readInt());
    }
}