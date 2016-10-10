package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.RegistryObject;
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
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.math.MathHelper;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyInt extends PropertyBase
{
    public static final String ID = "int";

    @RegistryObject(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyInt(0);

    private int value;
    private int minValue = Integer.MIN_VALUE;
    private int maxValue = Integer.MAX_VALUE;

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
    public String getID()
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
        value = MathHelper.clamp_int(v, getMin(), getMax());
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
    @Nullable
    public String getMinValueString()
    {
        int m = getMin();

        if(m != Integer.MIN_VALUE)
        {
            return Integer.toString(m);
        }

        return null;
    }

    @Override
    @Nullable
    public String getMaxValueString()
    {
        int m = getMax();

        if(m != Integer.MAX_VALUE)
        {
            return Integer.toString(m);
        }

        return null;
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        GuiSelectors.selectInt(null, getInt(), (id, val) ->
        {
            setInt(val);
            gui.onChanged(key, getSerializableElement());
            gui.openGui();
        });
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagInt(getInt());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        setInt(((NBTPrimitive) nbt).getInt());
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
    public void writeData(ByteBuf data, boolean extended)
    {
        data.writeInt(getInt());

        if(extended)
        {
            data.writeInt(getMin());
            data.writeInt(getMax());
        }
    }

    @Override
    public void readData(ByteBuf data, boolean extended)
    {
        setInt(data.readInt());

        if(extended)
        {
            setMin(data.readInt());
            setMax(data.readInt());
        }
    }
}