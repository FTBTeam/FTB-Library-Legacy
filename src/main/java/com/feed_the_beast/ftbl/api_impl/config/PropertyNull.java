package com.feed_the_beast.ftbl.api_impl.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import net.minecraft.nbt.NBTBase;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public enum PropertyNull implements IConfigValue
{
    INSTANCE;

    public static final String ID = "null";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> INSTANCE;

    @Override
    public String getID()
    {
        return ID;
    }

    @Override
    @Nullable
    public Object getValue()
    {
        return null;
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
    }

    @Override
    public String getString()
    {
        return "null";
    }

    @Override
    public boolean getBoolean()
    {
        return false;
    }

    @Override
    public int getInt()
    {
        return 0;
    }

    @Override
    public IConfigValue copy()
    {
        return INSTANCE;
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return value == this;
    }

    @Override
    public int getColor()
    {
        return 0;
    }

    @Override
    public double getDouble()
    {
        return 0;
    }

    @Nullable
    @Override
    public String getMinValueString()
    {
        return null;
    }

    @Nullable
    @Override
    public String getMaxValueString()
    {
        return null;
    }

    @Nullable
    @Override
    public List<String> getVariants()
    {
        return null;
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
    }

    @Override
    public void fromJson(JsonElement json)
    {
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return JsonNull.INSTANCE;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return null;
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
    }
}
