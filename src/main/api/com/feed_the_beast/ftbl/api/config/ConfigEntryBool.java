package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.gui.IClickable;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.io.ByteIOStream;
import net.minecraft.nbt.NBTTagByte;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Arrays;
import java.util.List;

public class ConfigEntryBool extends ConfigEntry implements IClickable, INBTSerializable<NBTTagByte>
{
    public boolean defValue;
    private boolean value;

    public ConfigEntryBool(boolean def)
    {
        defValue = def;
        value = def;
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.BOOLEAN;
    }

    @Override
    public int getColor()
    {
        return getAsBoolean() ? 0x33AA33 : 0xD52834;
    }

    public void set(boolean v)
    {
        value = v;
    }

    @Override
    public void fromJson(JsonElement o)
    {
        set(o.getAsBoolean());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsBoolean());
    }

    @Override
    public void onClicked(IMouseButton button)
    {
        set(!getAsBoolean());
    }

    @Override
    public String getAsString()
    {
        return getAsBoolean() ? "true" : "false";
    }

    @Override
    public boolean getAsBoolean()
    {
        return value;
    }

    @Override
    public int getAsInt()
    {
        return getAsBoolean() ? 1 : 0;
    }

    @Override
    public double getAsDouble()
    {
        return getAsBoolean() ? 1D : 0D;
    }

    @Override
    public String getDefValueString()
    {
        return defValue ? "true" : "false";
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryBool entry = new ConfigEntryBool(defValue);
        entry.set(getAsBoolean());
        return entry;
    }

    @Override
    public void writeData(ByteIOStream io, boolean extended)
    {
        super.writeData(io, extended);
        io.writeByte((getAsBoolean() ? 1 : 0) | ((defValue ? 1 : 0) << 1));
    }

    @Override
    public void readData(ByteIOStream io, boolean extended)
    {
        super.readData(io, extended);

        byte i = io.readByte();

        set((i & 1) != 0);

        if(extended)
        {
            defValue = (i & 2) != 0;
        }
    }

    @Override
    public List<String> getVariants()
    {
        return Arrays.asList("true", "false");
    }

    @Override
    public NBTTagByte serializeNBT()
    {
        return new NBTTagByte(getAsBoolean() ? (byte) 1 : 0);
    }

    @Override
    public void deserializeNBT(NBTTagByte nbt)
    {
        set(nbt.getByte() != 0);
    }
}