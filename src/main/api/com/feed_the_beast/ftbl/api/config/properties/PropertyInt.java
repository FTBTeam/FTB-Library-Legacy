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
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyInt implements IConfigValue, INumberBoundsContainer
{
    public enum Provider implements IConfigValueProvider
    {
        INSTANCE;

        public static final ResourceLocation ID = new ResourceLocation(FTBLibFinals.MOD_ID, "int");

        @Override
        public ResourceLocation getID()
        {
            return ID;
        }

        @Override
        public IConfigValue createDefault()
        {
            return new PropertyInt(0);
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
            double d = ((PropertyInt) value).getMin();

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
            double d = ((PropertyInt) value).getMax();

            if(d != Double.POSITIVE_INFINITY)
            {
                return LMStringUtils.formatDouble(d);
            }

            return null;
        }
    }

    private byte NBT_ID;
    private int value;
    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

    public PropertyInt(int nbtID, int v)
    {
        NBT_ID = (byte) nbtID;
        value = v;
    }

    public PropertyInt(int v)
    {
        this(Constants.NBT.TAG_INT, v);
    }

    @Override
    public IConfigValueProvider getProvider()
    {
        return Provider.INSTANCE;
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

    @Override
    public void setBounds(double min, double max)
    {
        setMin(min == Double.NEGATIVE_INFINITY ? Integer.MIN_VALUE : (int) min);
        setMax(max == Double.POSITIVE_INFINITY ? Integer.MAX_VALUE : (int) max);
    }

    @Override
    public double getMin()
    {
        return minValue == Integer.MIN_VALUE ? Double.NEGATIVE_INFINITY : minValue;
    }

    @Override
    public double getMax()
    {
        return maxValue == Integer.MAX_VALUE ? Double.POSITIVE_INFINITY : maxValue;
    }

    public void set(double v)
    {
        value = (int) MathHelper.clamp_double(v, getMin(), getMax());
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        data.writeByte(NBT_ID);

        switch(NBT_ID)
        {
            case Constants.NBT.TAG_BYTE:
                data.writeByte(getInt());
                return;
            case Constants.NBT.TAG_SHORT:
                data.writeShort(getInt());
                return;
            default:
                data.writeInt(getInt());
        }

        if(extended)
        {
            data.writeInt((int) getMin());
            data.writeInt((int) getMax());
        }
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        NBT_ID = data.readByte();

        switch(NBT_ID)
        {
            case Constants.NBT.TAG_BYTE:
                set(data.readByte());
                return;
            case Constants.NBT.TAG_SHORT:
                set(data.readShort());
                return;
            default:
                set(data.readInt());
        }

        if(extended)
        {
            setMin(data.readInt());
            setMax(data.readInt());
        }
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
        return new PropertyInt(getInt()).setMin((int) getMin()).setMax((int) getMax());
    }

    @Override
    public NBTBase serializeNBT()
    {
        switch(NBT_ID)
        {
            case Constants.NBT.TAG_BYTE:
                return new NBTTagByte((byte) getInt());
            case Constants.NBT.TAG_SHORT:
                return new NBTTagShort((short) getInt());
            default:
                return new NBTTagInt(getInt());
        }
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        set(((NBTPrimitive) nbt).getInt());
    }

    @Override
    public void fromJson(JsonElement json)
    {
        set(json.getAsInt());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getInt());
    }
}