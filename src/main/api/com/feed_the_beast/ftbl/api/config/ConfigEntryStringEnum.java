package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.gui.IClickable;
import com.feed_the_beast.ftbl.api.gui.IMouseButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.io.ByteIOStream;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ConfigEntryStringEnum extends ConfigEntry implements IClickable, INBTSerializable<NBTTagString>
{
    private final List<String> array;
    private int index;
    private int defValue;

    public ConfigEntryStringEnum()
    {
        array = new ArrayList<>();
    }

    public ConfigEntryStringEnum(Collection<String> vals, String def)
    {
        array = new ArrayList<>();
        array.addAll(vals);
        index = defValue = array.indexOf(def);
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.ENUM;
    }

    @Override
    public int getColor()
    {
        return 0x0094FF;
    }

    public void set(String s)
    {
        index = array.indexOf(s);
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int idx)
    {
        index = idx % array.size();

        if(index < 0)
        {
            index = array.size() - 1;
        }
    }

    @Override
    public void fromJson(@Nonnull JsonElement o)
    {
        set(o.getAsString());
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsString());
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryStringEnum entry = new ConfigEntryStringEnum(array, array.get(defValue));
        entry.setIndex(getIndex());
        return entry;
    }

    @Override
    public void writeData(ByteIOStream io, boolean extended)
    {
        super.writeData(io, extended);

        if(extended)
        {
            io.writeShort(array.size());

            if(!array.isEmpty())
            {
                for(String s : array)
                {
                    io.writeUTF(s);
                }
            }

            io.writeShort(defValue);
        }

        io.writeUTF(getAsString());
    }

    @Override
    public void readData(ByteIOStream io, boolean extended)
    {
        super.readData(io, extended);

        if(extended)
        {
            array.clear();

            int s = io.readUnsignedShort();

            for(int i = 0; i < s; i++)
            {
                array.add(io.readUTF());
            }

            defValue = io.readUnsignedShort();
        }

        set(io.readUTF());
    }

    @Override
    public void onClicked(@Nonnull IMouseButton button)
    {
        if(button.isLeft())
        {
            setIndex(getIndex() + 1);
        }
        else
        {
            setIndex(getIndex() - 1);
        }
    }

    @Override
    public String getAsString()
    {
        return array.get(index);
    }

    @Override
    public boolean getAsBoolean()
    {
        return getAsString() != null;
    }

    @Override
    public int getAsInt()
    {
        return index;
    }

    @Override
    public String getDefValueString()
    {
        return array.get(defValue);
    }

    @Override
    public List<String> getVariants()
    {
        List<String> list = new ArrayList<>(array.size());
        list.addAll(array);
        return list;
    }

    @Override
    public NBTTagString serializeNBT()
    {
        return new NBTTagString(getAsString());
    }

    @Override
    public void deserializeNBT(NBTTagString nbt)
    {
        set(nbt.getString());
    }
}