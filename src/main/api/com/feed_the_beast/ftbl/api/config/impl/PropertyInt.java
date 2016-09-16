package com.feed_the_beast.ftbl.api.config.impl;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.gui.GuiSelectField;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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
public class PropertyInt extends PropertyBase
{
    public static final ResourceLocation ID = new ResourceLocation(FTBLibFinals.MOD_ID, "int");

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

    public PropertyInt(int nbtID, int v, int min, int max)
    {
        this(nbtID, v);
        minValue = min;
        maxValue = max;
    }

    @Override
    public ResourceLocation getID()
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

    public void set(int v)
    {
        value = MathHelper.clamp_int(v, getMin(), getMax());
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
            data.writeInt(getMin());
            data.writeInt(getMax());
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
        return new PropertyInt(getInt()).setMin(getMin()).setMax(getMax());
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
    @Nullable
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
    @Nullable
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
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        GuiSelectField.display(null, GuiSelectField.FieldType.INTEGER, getInt(), (id, val) ->
        {
            set(val.hashCode());
            gui.onChanged(key, getSerializableElement());
            gui.openGui();
        });
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