package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.lib.gui.selectors.GuiSelectors;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyByte extends PropertyBase
{
    public static final String ID = "byte";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyByte((byte) 0);

    private byte value;
    private byte minValue = Byte.MIN_VALUE;
    private byte maxValue = Byte.MAX_VALUE;

    public PropertyByte(int v)
    {
        value = (byte) v;
    }

    public PropertyByte(int v, int min, int max)
    {
        this(v);
        minValue = (byte) min;
        maxValue = (byte) max;
    }

    @Override
    public String getID()
    {
        return ID;
    }

    public byte getByte()
    {
        return value;
    }

    public void setByte(byte v)
    {
        byte min = getMin();
        byte max = getMax();
        value = v < min ? min : (v > max ? max : v);
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getByte();
    }

    public PropertyByte setMin(byte v)
    {
        minValue = v;
        return this;
    }

    public PropertyByte setMax(byte v)
    {
        maxValue = v;
        return this;
    }

    public byte getMin()
    {
        return minValue;
    }

    public byte getMax()
    {
        return maxValue;
    }

    @Override
    public String getString()
    {
        return Short.toString(getByte());
    }

    @Override
    public boolean getBoolean()
    {
        return getByte() != 0;
    }

    @Override
    public int getInt()
    {
        return getByte();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyByte(getByte(), getMin(), getMax());
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return getByte() == value.getInt();
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
        byte m = getMin();

        if(m != Byte.MIN_VALUE)
        {
            return Byte.toString(m);
        }

        return null;
    }

    @Override
    @Nullable
    public String getMaxValueString()
    {
        byte m = getMax();

        if(m != Byte.MAX_VALUE)
        {
            return Byte.toString(m);
        }

        return null;
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        GuiSelectors.INSTANCE.selectInt(null, getByte(), (id, val) ->
        {
            setByte((byte) val);
            gui.onChanged(key, getSerializableElement());
            gui.openGui();
        });
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagByte(getByte());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        setByte(((NBTPrimitive) nbt).getByte());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setByte(json.getAsByte());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getByte());
    }

    @Override
    public void writeData(ByteBuf data, boolean extended)
    {
        data.writeByte(getByte());

        if(extended)
        {
            data.writeByte(getMin());
            data.writeByte(getMax());
        }
    }

    @Override
    public void readData(ByteBuf data, boolean extended)
    {
        setByte(data.readByte());

        if(extended)
        {
            setMin(data.readByte());
            setMax(data.readByte());
        }
    }
}