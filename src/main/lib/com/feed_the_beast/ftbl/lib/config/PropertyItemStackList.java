package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.lib.item.ItemStackSerializer;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 24.09.2016.
 */
public class PropertyItemStackList extends PropertyBase
{
    public static final String ID = "item_stack_list";

    private List<ItemStack> value;

    public PropertyItemStackList()
    {
        this(Collections.emptyList());
    }

    public PropertyItemStackList(Collection<ItemStack> l)
    {
        value = new ArrayList<>(l);
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

    /**
     * @param sizeMode 0 - ignore stack size, 1 - itemStack size must be equal, 2 - itemStack size must be equal or larger
     * @return stack that is equal to itemStack. null if none match
     */
    @Nullable
    public ItemStack containsItem(ItemStack itemStack, int sizeMode)
    {
        Item item0 = itemStack.getItem();
        int meta = itemStack.getMetadata();
        int stackSize = itemStack.stackSize;

        for(ItemStack is : getItems())
        {
            if(is.getItem() == item0 && is.getMetadata() == meta)
            {
                switch(sizeMode)
                {
                    case 1:
                        if(is.stackSize == stackSize)
                        {
                            return is;
                        }
                        break;
                    case 2:
                        if(is.stackSize <= stackSize)
                        {
                            return is;
                        }
                        break;
                    default:
                        return is;
                }
            }
        }

        return null;
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
        List<String> l = new ArrayList<>(getItems().size());

        for(ItemStack is : getItems())
        {
            l.add(is.stackSize + "x " + is.getDisplayName());
        }

        return l.toString();
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
        return new PropertyItemStackList(getItems());
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
    public void writeData(ByteBuf data)
    {
        value = getItems();
        data.writeShort(value.size());
        value.forEach(is -> LMNetUtils.writeItemStack(data, is));
    }

    @Override
    public void readData(ByteBuf data)
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