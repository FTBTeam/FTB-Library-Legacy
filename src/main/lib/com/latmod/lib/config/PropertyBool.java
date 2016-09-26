package com.latmod.lib.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyBool extends PropertyBase
{
    public static final String ID = "bool";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyBool(false);

    private boolean value;

    public PropertyBool(boolean v)
    {
        value = v;
    }

    @Override
    public String getID()
    {
        return ID;
    }

    @Override
    public boolean getBoolean()
    {
        return value;
    }

    public void setBoolean(boolean v)
    {
        value = v;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getBoolean();
    }

    @Override
    public String getString()
    {
        return value ? "true" : "false";
    }

    @Override
    public int getInt()
    {
        return getBoolean() ? 1 : 0;
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyBool(getBoolean());
    }

    @Override
    public boolean equalsValue(IConfigValue value)
    {
        return getBoolean() == value.getBoolean();
    }

    @Override
    public int getColor()
    {
        return getBoolean() ? 0x33AA33 : 0xD52834;
    }

    @Override
    public List<String> getVariants()
    {
        return Arrays.asList("true", "false");
    }

    @Override
    public void onClicked(IGuiEditConfig gui, IConfigKey key, IMouseButton button)
    {
        setBoolean(!getBoolean());
        gui.onChanged(key, getSerializableElement());
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagByte(getBoolean() ? (byte) 1 : 0);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        setBoolean(((NBTPrimitive) nbt).getByte() != 0);
    }

    @Override
    public void fromJson(JsonElement json)
    {
        setBoolean(json.getAsBoolean());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getBoolean());
    }

    @Override
    public void writeData(ByteBuf data, boolean extended)
    {
        data.writeBoolean(getBoolean());
    }

    @Override
    public void readData(ByteBuf data, boolean extended)
    {
        setBoolean(data.readBoolean());
    }
}