package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.net.MessageLM;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.util.LMListUtils;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import io.netty.buffer.ByteBuf;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ConfigEntryStringList extends ConfigEntry
{
    public final List<String> defValue;
    private List<String> value;

    public ConfigEntryStringList(List<String> def)
    {
        value = new ArrayList<>();
        defValue = new ArrayList<>();

        if(def != null && !def.isEmpty())
        {
            defValue.addAll(def);
            value.addAll(def);
        }
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.STRING_ARRAY;
    }

    @Override
    public int getColor()
    {
        return 0xFFAA49;
    }

    public void set(List<String> o)
    {
        value.clear();

        if(o != null && !o.isEmpty())
        {
            value.addAll(o);
        }
    }

    @Override
    public final void fromJson(@Nonnull JsonElement o)
    {
        JsonArray a = o.getAsJsonArray();
        value.clear();
        for(int i = 0; i < a.size(); i++)
        {
            value.add(a.get(i).getAsString());
        }
        set(LMListUtils.clone(value));
    }

    @Nonnull
    @Override
    public final JsonElement getSerializableElement()
    {
        JsonArray a = new JsonArray();
        for(String aValue : getAsStringList())
        {
            a.add(new JsonPrimitive(aValue));
        }
        return a;
    }

    @Override
    public String getAsString()
    {
        return getAsStringList().toString();
    }

    @Override
    public boolean getAsBoolean()
    {
        return !getAsStringList().isEmpty();
    }

    @Override
    public TIntList getAsIntList()
    {
        List<String> list = getAsStringList();
        TIntList l = new TIntArrayList(list.size());
        for(String s : getAsStringList())
        {
            l.add(Integer.parseInt(s));
        }

        return l;
    }

    @Override
    public List<String> getAsStringList()
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
        ConfigEntryStringList entry = new ConfigEntryStringList(defValue);
        entry.set(getAsStringList());
        return entry;
    }

    @Override
    public void writeData(ByteBuf io, boolean extended)
    {
        super.writeData(io, extended);
        List<String> list = getAsStringList();
        int s = list.size();

        io.writeInt(s);

        for(int i = 0; i < s; i++)
        {
            MessageLM.writeString(io, list.get(i));
        }

        if(extended)
        {
            s = defValue.size();
            io.writeInt(s);

            for(int i = 0; i < s; i++)
            {
                MessageLM.writeString(io, defValue.get(i));
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
            List<String> list = new ArrayList<>(s);
            for(int i = 0; i < s; i++)
            {
                list.add(MessageLM.readString(io));
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
                    defValue.add(MessageLM.readString(io));
                }
            }
        }
    }
}