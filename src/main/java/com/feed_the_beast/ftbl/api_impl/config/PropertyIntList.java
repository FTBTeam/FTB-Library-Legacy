package com.feed_the_beast.ftbl.api_impl.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.google.gson.JsonElement;
import com.latmod.lib.util.LMJsonUtils;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 15.09.2016.
 */
public class PropertyIntList extends PropertyBase
{
    public static final String ID = "int_list";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = PropertyIntList::new;

    private TIntList value;

    public PropertyIntList(TIntList v)
    {
        value = v;
    }

    public PropertyIntList(int... ai)
    {
        this(TIntArrayList.wrap(ai));
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
        return getIntList();
    }

    public void set(TIntList v)
    {
        value = v;
    }

    public TIntList getIntList()
    {
        return value;
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        TIntList list = getIntList();

        data.writeShort(list.size());

        if(!list.isEmpty())
        {
            data.writeShort(list.size());

            for(int i = 0; i < list.size(); i++)
            {
                data.writeInt(list.get(i));
            }
        }
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        int s = data.readShort() & 0xFFFF;
        value.clear();

        for(int i = 0; i < s; i++)
        {
            value.add(data.readInt());
        }

        set(value);
    }

    @Override
    public String getString()
    {
        return getIntList().toString();
    }

    @Override
    public boolean getBoolean()
    {
        return !getIntList().isEmpty();
    }

    @Override
    public int getInt()
    {
        return getIntList().size();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyIntList(new TIntArrayList(getIntList()));
    }

    @Override
    public int getColor()
    {
        return 0xFFAA49;
    }

    @Override
    public List<String> getVariants()
    {
        TIntList list0 = getIntList();
        List<String> list = new ArrayList<>(list0.size());
        list0.forEach(i -> list.add(Integer.toString(i)));
        return list;
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagIntArray(getIntList().toArray());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        set(TIntArrayList.wrap(((NBTTagIntArray) nbt).getIntArray()));
    }

    @Override
    public void fromJson(JsonElement json)
    {
        set(TIntArrayList.wrap(LMJsonUtils.fromIntArray(json)));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return LMJsonUtils.toIntArray(getIntList().toArray());
    }
}