package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;

public final class ConfigEntryEnum<E extends Enum<E>> extends ConfigEntry implements IClickable // EnumTypeAdapterFactory
{
    public final int defValue;
    public final EnumNameMap<E> nameMap;
    private int index;

    public ConfigEntryEnum(String id, E def, EnumNameMap<E> map)
    {
        super(id);
        nameMap = map;
        defValue = nameMap.getIndex(def);
        index = defValue;
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.ENUM;
    }

    @Override
    public int getColor()
    {
        return 0x0094FF;
    }

    public void set(Object o)
    {
        index = nameMap.getIndex(o);
    }

    public E get()
    {
        return nameMap.getFromIndex(index);
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int idx)
    {
        index = idx;

        if(index < 0 || index >= nameMap.size)
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    @Override
    public void fromJson(JsonElement o)
    {
        set(nameMap.get(o.getAsString()));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(EnumNameMap.getEnumName(get()));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);

        if(extended)
        {
            NBTTagList list = new NBTTagList();

            for(E e : nameMap.getValues())
            {
                list.appendTag(new NBTTagString(EnumNameMap.getEnumName(e)));
            }

            tag.setTag("VL", list);
            tag.setShort("V", (short) index);
            tag.setShort("D", (short) defValue);
        }
        else
        {
            tag.setString("V", EnumNameMap.getEnumName(get()));
        }

        System.out.println(getFullID() + ": TXE " + tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        super.readFromNBT(tag, extended);

        if(extended)
        {
            index = tag.getShort("V");
        }
        else
        {
            index = nameMap.getStringIndex(tag.getString("V"));
        }
    }

    @Override
    public void onClicked(MouseButton button)
    {
        if(button.isLeft())
        {
            index = (index + 1) % nameMap.size;
        }
        else
        {
            if(--index < 0)
            {
                index = nameMap.size - 1;
            }
        }
    }

    @Override
    public String getAsString()
    {
        return EnumNameMap.getEnumName(get());
    }

    @Override
    public boolean getAsBoolean()
    {
        return get() != null;
    }

    @Override
    public int getAsInt()
    {
        return getIndex();
    }

    @Override
    public String getDefValueString()
    {
        return EnumNameMap.getEnumName(nameMap.getFromIndex(defValue));
    }

    public boolean isDefault()
    {
        return index == defValue;
    }

    @Override
    public List<String> getVariants()
    {
        List<String> list = new ArrayList<>(nameMap.size);
        list.addAll(nameMap.getKeys());
        return list;
    }
}