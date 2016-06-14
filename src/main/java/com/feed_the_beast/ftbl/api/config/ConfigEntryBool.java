package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

public class ConfigEntryBool extends ConfigEntry implements IClickable
{
    public boolean defValue;
    private boolean value;

    public ConfigEntryBool(boolean def)
    {
        set(def);
        defValue = def;
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
    public void fromJson(@Nonnull JsonElement o)
    {
        set(o.getAsBoolean());
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsBoolean());
    }

    @Override
    public void onClicked(@Nonnull MouseButton button)
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
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);
        tag.setBoolean("V", getAsBoolean());

        if(extended)
        {
            tag.setBoolean("D", defValue);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        super.readFromNBT(tag, extended);
        set(tag.getBoolean("V"));

        if(extended)
        {
            defValue = tag.getBoolean("D");
        }
    }

    @Override
    public List<String> getVariants()
    {
        return Arrays.asList("true", "false");
    }

    @Override
    public boolean hasDiff(ConfigEntry entry)
    {
        return entry.getAsBoolean() != getAsBoolean();
    }
}