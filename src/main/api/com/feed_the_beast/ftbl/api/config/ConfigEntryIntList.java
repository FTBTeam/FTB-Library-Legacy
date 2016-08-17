package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.io.ByteCount;
import com.latmod.lib.io.ByteIOStream;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ConfigEntryIntList extends ConfigEntry implements INBTSerializable<NBTTagIntArray>
{
    public final TIntList defValue;
    private TIntList value;

    public ConfigEntryIntList(TIntList def)
    {
        defValue = def == null ? new TIntArrayList() : def;
        value = new TIntArrayList();
        value.addAll(defValue);
    }

    public ConfigEntryIntList(int[] def)
    {
        this(TIntArrayList.wrap(def));
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.INT_ARRAY;
    }

    @Override
    public int getColor()
    {
        return 0xAA5AE8;
    }

    public void set(TIntList l)
    {
        value.clear();

        if(l != null && !l.isEmpty())
        {
            value.addAll(l);
        }
    }

    @Override
    public void fromJson(@Nonnull JsonElement o)
    {
        JsonArray a = o.getAsJsonArray();
        TIntList l = new TIntArrayList(a.size());
        for(int i = 0; i < l.size(); i++)
        {
            l.set(i, a.get(i).getAsInt());
        }

        set(l);
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        JsonArray a = new JsonArray();
        value = getAsIntList();
        for(int i = 0; i < value.size(); i++)
        {
            a.add(new JsonPrimitive(value.get(i)));
        }
        return a;
    }

    @Override
    public String getAsString()
    {
        return getAsIntList().toString();
    }

    @Override
    public List<String> getAsStringList()
    {
        TIntList list = getAsIntList();

        List<String> l = new ArrayList<>(list.size());

        for(int i = 0; i < list.size(); i++)
        {
            l.add(Integer.toString(list.get(i)));
        }

        return l;
    }

    @Override
    public TIntList getAsIntList()
    {
        return value;
    }

    @Override
    public String getDefValueString()
    {
        return defValue.toString();
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryIntList entry = new ConfigEntryIntList(defValue);
        entry.set(getAsIntList());
        return entry;
    }

    @Override
    public void writeData(ByteIOStream io, boolean extended)
    {
        super.writeData(io, extended);
        io.writeIntArray(getAsIntList().toArray(), ByteCount.INT);

        if(extended)
        {
            io.writeIntArray(defValue.toArray(), ByteCount.INT);
        }
    }

    @Override
    public void readData(ByteIOStream io, boolean extended)
    {
        super.readData(io, extended);

        set(TIntArrayList.wrap(io.readIntArray(ByteCount.INT)));

        if(extended)
        {
            defValue.clear();
            defValue.add(io.readIntArray(ByteCount.INT));
        }
    }

    @Override
    public NBTTagIntArray serializeNBT()
    {
        return new NBTTagIntArray(getAsIntList().toArray());
    }

    @Override
    public void deserializeNBT(NBTTagIntArray nbt)
    {
        set(TIntArrayList.wrap(nbt.getIntArray()));
    }
}