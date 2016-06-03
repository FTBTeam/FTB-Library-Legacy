package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ConfigEntryEnum<E extends Enum<E>> extends ConfigEntry implements IClickable // EnumTypeAdapterFactory
{
    public final int defValue;
    private final List<E> array;
    private int index;

    public ConfigEntryEnum(String id, E[] val, E def, boolean addNull)
    {
        super(id);

        array = new ArrayList<>(addNull ? val.length + 1 : val.length);
        array.addAll(Arrays.asList(val));

        if(addNull)
        {
            array.add(null);
        }

        set(def);
        defValue = indexOf(def);
    }

    public static String getName(Enum<?> e)
    {
        return e == null ? "-" : e.name().toLowerCase();
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

    public final int indexOf(Object o)
    {
        return array.indexOf(o);
    }

    public void set(Object o)
    {
        index = indexOf(o);
    }

    public E get()
    {
        return array.get(index);
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int idx)
    {
        index = idx;

        if(index < 0 || index >= array.size())
        {
            throw new ArrayIndexOutOfBoundsException(index);
        }
    }

    public E fromIndex(int idx)
    {
        return array.get(idx);
    }

    public E fromString(String s)
    {
        if(s.isEmpty() || s.charAt(0) == '-')
        {
            return null;
        }

        for(E e : array)
        {
            if(e != null && e.name().equalsIgnoreCase(s))
            {
                return e;
            }
        }

        return null;
    }

    @Override
    public void fromJson(JsonElement o)
    {
        set(fromString(o.getAsString()));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getName(get()));
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);

        if(extended)
        {
            NBTTagList list = new NBTTagList();

            for(E e : array)
            {
                list.appendTag(new NBTTagString(getName(e)));
            }

            tag.setTag("VL", list);
            tag.setShort("V", (short) index);
            tag.setShort("D", (short) defValue);
        }
        else
        {
            tag.setString("V", getName(get()));
        }
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
            index = indexOf(fromString(tag.getString("V")));
        }
    }

    @Override
    public void onClicked(MouseButton button)
    {
        if(button.isLeft())
        {
            index = (index + 1) % array.size();
        }
        else
        {
            if(--index < 0)
            {
                index = array.size() - 1;
            }
        }
    }

    @Override
    public String getAsString()
    {
        return getName(get());
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
        return getName(fromIndex(defValue));
    }

    public boolean isDefault()
    {
        return index == defValue;
    }
}