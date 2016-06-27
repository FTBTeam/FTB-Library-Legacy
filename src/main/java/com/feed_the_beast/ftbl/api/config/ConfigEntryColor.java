package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.LMColor;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;

public class ConfigEntryColor extends ConfigEntry
{
    public final LMColor.RGB value;
    public final LMColor.RGB defValue;

    public ConfigEntryColor(LMColor def)
    {
        value = new LMColor.RGB();
        value.set(def);

        defValue = new LMColor.RGB();
        defValue.set(def);
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.COLOR;
    }

    @Override
    public int getColor()
    {
        return value.color();
    }

    @Override
    public void fromJson(@Nonnull JsonElement o)
    {
        value.setRGBA(o.getAsInt());
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(value.color());
    }

    @Override
    public String getAsString()
    {
        return value.toString();
    }

    @Override
    public int getAsInt()
    {
        return value.color();
    }

    @Override
    public String getDefValueString()
    {
        return defValue.toString();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);
        tag.setInteger("V", value.color());

        if(extended)
        {
            tag.setInteger("D", defValue.color());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        super.readFromNBT(tag, extended);
        value.setRGBA(tag.getInteger("V"));

        if(extended)
        {
            defValue.setRGBA(tag.getInteger("D"));
        }
    }
}