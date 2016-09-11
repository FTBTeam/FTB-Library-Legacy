package com.feed_the_beast.ftbl.api.config.properties;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.util.ResourceLocation;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyBool implements IConfigValue
{
    public enum Provider implements IConfigValueProvider
    {
        INSTANCE;

        public static final ResourceLocation ID = new ResourceLocation(FTBLibFinals.MOD_ID, "bool");

        @Override
        public ResourceLocation getID()
        {
            return ID;
        }

        @Override
        public IConfigValue createDefault()
        {
            return new PropertyBool(false);
        }

        @Override
        public int getColor(IConfigValue value)
        {
            return value.getBoolean() ? 0x33AA33 : 0xD52834;
        }

        @Override
        public List<String> getVariants()
        {
            return Arrays.asList("true", "false");
        }

        @Override
        public boolean onClicked(IConfigValue value, IMouseButton button)
        {
            ((PropertyBool) value).set(!value.getBoolean());
            return true;
        }
    }

    private boolean value;

    public PropertyBool(boolean v)
    {
        value = v;
    }

    @Override
    public IConfigValueProvider getProvider()
    {
        return Provider.INSTANCE;
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