package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.nbt.NBTTagCompound;

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
    public void fromJson(JsonElement o)
    {
        JsonArray a = o.getAsJsonArray();
        TIntList l = new TIntArrayList(a.size());
        for(int i = 0; i < l.size(); i++)
        {
            l.set(i, a.get(i).getAsInt());
        }

        set(l);
    }

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
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);

        int[] ai = getAsIntList().toArray();

        if(ai.length > 0)
        {
            tag.setIntArray("V", ai);
        }

        if(extended && !defValue.isEmpty())
        {
            tag.setIntArray("D", defValue.toArray());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        super.readFromNBT(tag, extended);
        set(tag.hasKey("V") ? TIntArrayList.wrap(tag.getIntArray("V")) : null);

        if(extended)
        {
            defValue.clear();

            if(tag.hasKey("D"))
            {
                defValue.addAll(tag.getIntArray("D"));
            }
        }
    }

    @Override
    public boolean hasDiff(ConfigEntry entry)
    {
        TIntList l = entry.getAsIntList();
        value = getAsIntList();

        if(l.size() != value.size())
        {
            return true;
        }

        for(int i = 0; i < value.size(); i++)
        {
            if(l.get(i) != value.get(i))
            {
                return true;
            }
        }

        return false;
    }
}