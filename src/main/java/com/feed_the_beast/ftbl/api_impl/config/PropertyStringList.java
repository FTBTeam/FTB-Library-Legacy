package com.feed_the_beast.ftbl.api_impl.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import scala.actors.threadpool.*;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 26.08.2016.
 */
public class PropertyStringList extends PropertyBase
{
    public static final String ID = "string_list";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyStringList(Collections.emptyList());

    private List<String> value;

    public PropertyStringList(List<String> v)
    {
        value = v;
    }

    public PropertyStringList(String... s)
    {
        this(Arrays.asList(s));
    }

    @Override
    public String getID()
    {
        return ID;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getStringList();
    }

    public void set(List<String> v)
    {
        value = v;
    }

    public List<String> getStringList()
    {
        return value;
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        List<String> list = getStringList();

        data.writeShort(list.size());

        if(!list.isEmpty())
        {
            data.writeShort(list.size());

            for(String s : list)
            {
                data.writeUTF(s);
            }
        }
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        int s = data.readShort() & 0xFFFF;

        if(s <= 0)
        {
            set(Collections.emptyList());
        }
        else
        {
            List<String> list = new ArrayList<>(s);

            for(int i = 0; i < s; i++)
            {
                list.add(data.readUTF());
            }

            set(list);
        }
    }

    @Override
    public String getString()
    {
        return getStringList().toString();
    }

    @Override
    public boolean getBoolean()
    {
        return !getStringList().isEmpty();
    }

    @Override
    public int getInt()
    {
        return getStringList().size();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyStringList(new ArrayList<>(getStringList()));
    }

    @Override
    public int getColor()
    {
        return 0xFFAA49;
    }

    @Override
    public List<String> getVariants()
    {
        List<String> list0 = getStringList();
        List<String> list = new ArrayList<>(list0.size());
        list.addAll(list0);
        return list;
    }

    @Override
    public NBTBase serializeNBT()
    {
        NBTTagList tagList = new NBTTagList();

        for(String s : getStringList())
        {
            tagList.appendTag(new NBTTagString(s));
        }

        return tagList;
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        NBTTagList tagList = (NBTTagList) nbt;

        int s = tagList.tagCount();

        if(s <= 0)
        {
            set(Collections.emptyList());
        }
        else
        {
            List<String> list = new ArrayList<>(s);

            for(int i = 0; i < s; i++)
            {
                list.add(tagList.getStringTagAt(i));
            }

            set(list);
        }
    }

    @Override
    public void fromJson(JsonElement json)
    {
        JsonArray a = json.getAsJsonArray();

        if(a.size() == 0)
        {
            set(Collections.emptyList());
        }
        else
        {
            List<String> list = new ArrayList<>(a.size());

            for(JsonElement e : a)
            {
                list.add(e.getAsString());
            }

            set(list);
        }
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getString());
    }
}