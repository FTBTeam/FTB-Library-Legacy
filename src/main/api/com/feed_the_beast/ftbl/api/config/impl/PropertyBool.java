package com.feed_the_beast.ftbl.api.config.impl;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IGuiEditConfig;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyBool extends PropertyBase
{
    public static final ResourceLocation ID = new ResourceLocation(FTBLibFinals.MOD_ID, "bool");

    private boolean value;

    public PropertyBool(boolean v)
    {
        value = v;
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
        return getBoolean();
    }

    public void set(boolean v)
    {
        value = v;
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        data.writeBoolean(getBoolean());
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        set(data.readBoolean());
    }

    @Override
    public String getString()
    {
        return value ? "true" : "false";
    }

    @Override
    public boolean getBoolean()
    {
        return value;
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
        set(!getBoolean());
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagByte(getBoolean() ? (byte) 1 : 0);
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        set(((NBTPrimitive) nbt).getByte() != 0);
    }

    @Override
    public void fromJson(JsonElement json)
    {
        set(json.getAsBoolean());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getBoolean());
    }
}