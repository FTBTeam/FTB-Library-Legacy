package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ConfigEntryIntList extends ConfigEntry
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
    public void writeData(ByteBuf io, boolean extended)
    {
        super.writeData(io, extended);
        TIntList list = getAsIntList();
        int s = list.size();

        io.writeInt(s);

        for(int i = 0; i < s; i++)
        {
            io.writeInt(list.get(i));
        }

        if(extended)
        {
            s = defValue.size();
            io.writeInt(s);

            for(int i = 0; i < s; i++)
            {
                io.writeInt(defValue.get(i));
            }
        }
    }

    @Override
    public void readData(ByteBuf io, boolean extended)
    {
        super.readData(io, extended);
        int s = io.readInt();

        if(s == 0)
        {
            set(null);
        }
        else
        {
            TIntList list = new TIntArrayList(s);
            for(int i = 0; i < s; i++)
            {
                list.add(io.readInt());
            }

            set(list);
        }

        if(extended)
        {
            defValue.clear();

            s = io.readInt();

            if(s > 0)
            {
                for(int i = 0; i < s; i++)
                {
                    defValue.add(io.readInt());
                }
            }
        }
    }
}