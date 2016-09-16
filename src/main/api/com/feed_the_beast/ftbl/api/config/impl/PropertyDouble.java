package com.feed_the_beast.ftbl.api.config.impl;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.feed_the_beast.ftbl.gui.GuiSelectField;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
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
public class PropertyDouble extends PropertyBase
{
    public static final ResourceLocation ID = new ResourceLocation(FTBLibFinals.MOD_ID, "double");

    private double value;
    private double min = Double.MIN_VALUE;
    private double max = Double.MAX_VALUE;

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
    public ResourceLocation getID()
    {
        return ID;
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

    @Override
    public double getDouble()
    {
        return value;
    }

    public double getMin()
    {
        return min;
    }

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
    @Nullable
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
    @Nullable
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
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        GuiSelectField.display(null, GuiSelectField.FieldType.DOUBLE, getDouble(), (id, val) ->
        {
            set((Double) val);
            gui.onChanged(key, getSerializableElement());
            gui.openGui();
        });
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