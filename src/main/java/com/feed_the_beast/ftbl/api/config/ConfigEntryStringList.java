package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import latmod.lib.LMListUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

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
    public final void fromJson(JsonElement o)
    {
        JsonArray a = o.getAsJsonArray();
        value.clear();
        for(int i = 0; i < a.size(); i++)
        {
            value.add(a.get(i).getAsString());
        }
        set(LMListUtils.clone(value));
    }

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
        for(int i = 0; i < list.size(); i++)
        {
            l.add(Integer.parseInt(value.get(i)));
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
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);

        List<String> list = getAsStringList();

        if(!list.isEmpty())
        {
            NBTTagList l = new NBTTagList();

            for(String s : list)
            {
                l.appendTag(new NBTTagString(s));
            }

            tag.setTag("V", l);
        }

        if(extended && !defValue.isEmpty())
        {
            NBTTagList l = new NBTTagList();

            for(String s : defValue)
            {
                l.appendTag(new NBTTagString(s));
            }

            tag.setTag("D", l);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        super.readFromNBT(tag, extended);

        NBTTagList list = (NBTTagList) tag.getTag("V");

        if(list != null)
        {
            List<String> l = new ArrayList<>(list.tagCount());

            for(int i = 0; i < list.tagCount(); i++)
            {
                l.add(list.getStringTagAt(i));
            }

            set(l);
        }
        else
        {
            set(null);
        }

        if(extended)
        {
            defValue.clear();

            list = (NBTTagList) tag.getTag("D");

            if(list != null)
            {
                for(int i = 0; i < list.tagCount(); i++)
                {
                    defValue.add(list.getStringTagAt(i));
                }
            }
        }
    }

    @Override
    public boolean hasDiff(ConfigEntry entry)
    {
        List<String> l = entry.getAsStringList();
        value = getAsStringList();

        if(l.size() != value.size())
        {
            return true;
        }

        for(int i = 0; i < value.size(); i++)
        {
            if(!l.get(i).equals(value.get(i)))
            {
                return true;
            }
        }

        return false;
    }
}