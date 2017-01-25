package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiSelectors;
import com.feed_the_beast.ftbl.lib.math.Converter;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagShort;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyShort extends PropertyBase
{
    public static final String ID = "short";

    private short value;
    private short minValue = Short.MIN_VALUE;
    private short maxValue = Short.MAX_VALUE;

    public PropertyShort()
    {
    }

    public PropertyShort(int v)
    {
        value = (short) v;
    }

    public PropertyShort(int v, int min, int max)
    {
        this(v);
        minValue = (short) min;
        maxValue = (short) max;
    }

    @Override
    public String getName()
    {
        return ID;
    }

    public short getShort()
    {
        return value;
    }

    public void setShort(short v)
    {
        short min = getMin();
        short max = getMax();
        value = v < min ? min : (v > max ? max : v);
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getShort();
    }

    public PropertyShort setMin(short v)
    {
        minValue = v;
        return this;
    }

    public PropertyShort setMax(short v)
    {
        maxValue = v;
        return this;
    }

    public short getMin()
    {
        return minValue;
    }

    public short getMax()
    {
        return maxValue;
    }

    @Override
    public String getString()
    {
        return Short.toString(getShort());
    }

    @Override
    public boolean getBoolean()
    {
        return getShort() != 0;
    }

    @Override
    public int getInt()
    {
        return getShort();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyShort(getShort(), getMin(), getMax());
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return getShort() == value.getInt();
    }

    @Override
    public int getColor()
    {
        return 0xAA5AE8;
    }

    @Override
    @Nullable
    public String getMinValueString()
    {
        short m = getMin();

        if(m != Short.MIN_VALUE)
        {
            return Short.toString(m);
        }

        return null;
    }

    @Override
    @Nullable
    public String getMaxValueString()
    {
        short m = getMax();

        if(m != Short.MAX_VALUE)
        {
            return Short.toString(m);
        }

        return null;
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        GuiSelectors.selectJson(this, (val, set) ->
        {
            if(set)
            {
                setShort((short) val.getInt());
                gui.onChanged(key, getSerializableElement());
            }

            gui.openGui();
        });
    }

    @Override
    public boolean canParse(String text)
    {
        return Converter.canParseInt(text);
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagShort(getShort());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        setShort(((NBTPrimitive) nbt).getShort());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setShort(json.getAsShort());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getShort());
    }

    @Override
    public void writeData(ByteBuf data)
    {
        data.writeShort(getShort());
        data.writeShort(getMin());
        data.writeShort(getMax());
    }

    @Override
    public void readData(ByteBuf data)
    {
        setShort(data.readShort());
        setMin(data.readShort());
        setMax(data.readShort());
    }
}