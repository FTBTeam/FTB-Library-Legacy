package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.gui.IClickable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.util.ArrayList;
import java.util.List;

public final class ConfigEntryStringEnum extends ConfigEntry implements IClickable
{
    private final List<String> array;
    private int index;
    private int defValue;

    public ConfigEntryStringEnum(String id)
    {
        super(id);
        array = new ArrayList<>();
    }

    public ConfigEntryStringEnum(String id, List<String> vals, String def)
    {
        super(id);
        array = vals;
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
        index = idx;
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
            tag.setShort("V", (short) index);
            tag.setShort("D", (short) defValue);

            if(!array.isEmpty())
            {
                NBTTagList list = new NBTTagList();

                for(String s : array)
                {
                    list.appendTag(new NBTTagString(s));
                }

                tag.setTag("VL", list);
            }
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
            index = tag.getShort("V") & 0xFFFF;
            defValue = tag.getShort("D") & 0xFFFF;

            array.clear();

            if(tag.hasKey("VL"))
            {
                NBTTagList list = (NBTTagList) tag.getTag("VL");

                for(int i = 0; i < list.tagCount(); i++)
                {
                    array.add(list.getStringTagAt(i));
                }
            }
        }
        else
        {
            index = array.indexOf(tag.getString("V"));
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
        return getIndex();
    }

    @Override
    public String getDefValueString()
    {
        return array.get(defValue);
    }
}