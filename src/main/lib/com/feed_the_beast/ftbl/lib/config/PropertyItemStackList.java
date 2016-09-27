package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LatvianModder on 24.09.2016.
 */
public class PropertyItemStackList extends PropertyBase
{
    public static final String ID = "item_stack_list";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyItemStackList(new ArrayList<>());

    private List<ItemStack> value;

    public PropertyItemStackList(List<ItemStack> l)
    {
        value = l;
    }

    @Override
    public String getID()
    {
        return ID;
    }

    public List<ItemStack> getItems()
    {
        return value;
    }

    public void setItems(List<ItemStack> list)
    {
        value = list;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getItems();
    }

    @Override
    public String getString()
    {
        return getItems().toString();
    }

    @Override
    public boolean getBoolean()
    {
        return !getItems().isEmpty();
    }

    @Override
    public int getInt()
    {
        return getItems().size();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyItemStackList(new ArrayList<>(getItems()));
    }

    @Override
    public NBTBase serializeNBT()
    {
        NBTTagList list = new NBTTagList();
        getItems().forEach(item -> list.appendTag(item.serializeNBT()));
        return list;
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        value.clear();
        NBTTagList list = (NBTTagList) nbt;

        for(int i = 0; i < list.tagCount(); i++)
        {
            value.add(ItemStack.loadItemStackFromNBT(list.getCompoundTagAt(i)));
        }

        setItems(value);
    }

    @Override
    public void fromJson(JsonElement o)
    {
        value.clear();

        if(o.isJsonArray())
        {
            for(JsonElement e : o.getAsJsonArray())
            {
                ItemStack is = ItemStackSerializer.deserialize(e);

                if(is != null)
                {
                    value.add(is);
                }
            }
        }

        setItems(value);
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonArray a = new JsonArray();
        getItems().forEach(item -> a.add(ItemStackSerializer.serialize(item)));
        return a;
    }

    @Override
    public void writeData(ByteBuf data, boolean extended)
    {
        value = getItems();
        data.writeShort(value.size());
        value.forEach(is -> LMNetUtils.writeItemStack(data, is));
    }

    @Override
    public void readData(ByteBuf data, boolean extended)
    {
        value.clear();
        int s = data.readUnsignedShort();

        while(--s >= 0)
        {
            value.add(LMNetUtils.readItemStack(data));
        }

        setItems(value);
    }
}