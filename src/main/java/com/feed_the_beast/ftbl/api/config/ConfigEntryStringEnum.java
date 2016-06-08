package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class ConfigEntryStringEnum extends ConfigEntry implements IClickable
{
    private final List<String> array;
    private int index;
    private int defValue;

    public ConfigEntryStringEnum()
    {
        array = new ArrayList<>();
    }

    public ConfigEntryStringEnum(Collection<String> vals, String def)
    {
        array = new ArrayList<>();
        array.addAll(vals);
        index = defValue = array.indexOf(def);
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

    public void set(String s)
    {
        index = array.indexOf(s);
    }

    public int getIndex()
    {
        return index;
    }

    public void setIndex(int idx)
    {
        index = idx % array.size();

        if(index < 0)
        {
            index = array.size() - 1;
        }
    }

    @Override
    public void fromJson(JsonElement o)
    {
        set(o.getAsString());
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getAsString());
    }

    @Override
    public void writeToNBT(NBTTagCompound tag, boolean extended)
    {
        super.writeToNBT(tag, extended);

        if(extended)
        {
            if(!array.isEmpty())
            {
                NBTTagList list = new NBTTagList();

                for(String s : array)
                {
                    list.appendTag(new NBTTagString(s));
                }

                tag.setTag("VL", list);
            }

            tag.setShort("V", (short) index);
            tag.setShort("D", (short) defValue);
        }
        else
        {
            tag.setString("V", getAsString());
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag, boolean extended)
    {
        super.readFromNBT(tag, extended);

        if(extended)
        {
            array.clear();

            if(tag.hasKey("VL"))
            {
                NBTTagList list = (NBTTagList) tag.getTag("VL");

                for(int i = 0; i < list.tagCount(); i++)
                {
                    array.add(list.getStringTagAt(i));
                }
            }

            index = tag.getShort("V");
            defValue = tag.getShort("D");
        }
        else
        {
            set(tag.getString("V"));
        }
    }

    @Override
    public void onClicked(MouseButton button)
    {
        if(button.isLeft())
        {
            setIndex(getIndex() + 1);
        }
        else
        {
            setIndex(getIndex() - 1);
        }
    }

    @Override
    public String getAsString()
    {
        return array.get(index);
    }

    @Override
    public boolean getAsBoolean()
    {
        return getAsString() != null;
    }

    @Override
    public int getAsInt()
    {
        return index;
    }

    @Override
    public String getDefValueString()
    {
        return array.get(defValue);
    }

    @Override
    public List<String> getVariants()
    {
        List<String> list = new ArrayList<>(array.size());
        list.addAll(array);
        return list;
    }
}