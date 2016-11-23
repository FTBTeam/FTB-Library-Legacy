package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.google.gson.JsonElement;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagIntArray;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 15.09.2016.
 */
public class PropertyIntList extends PropertyBase
{
    public static final String ID = "int_list";

    private TIntList value;

    public PropertyIntList()
    {
        this(new TIntArrayList(0));
    }

    public PropertyIntList(TIntList v)
    {
        value = new TIntArrayList(v);
    }

    public PropertyIntList(int... ai)
    {
        value = new TIntArrayList(ai);
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
    public boolean containsInt(int val)
    {
        return getIntList().contains(val);
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyIntList(getIntList());
    }

    @Override
    public int getColor()
    {
        return 0xFFAA49;
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

    @Override
    public void writeData(ByteBuf data)
    {
        TIntList list = getIntList();

        data.writeShort(list.size());

        if(!list.isEmpty())
        {
            for(int i = 0; i < list.size(); i++)
            {
                data.writeInt(list.get(i));
            }
        }
    }

    @Override
    public void readData(ByteBuf data)
    {
        int s = data.readUnsignedShort();
        value.clear();

        while(--s >= 0)
        {
            value.add(data.readInt());
        }

        set(value);
    }
}