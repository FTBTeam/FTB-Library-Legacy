package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.MouseButton;
import com.feed_the_beast.ftbl.api.client.gui.IClickable;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.json.JsonElementIO;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ConfigEntryCustom extends ConfigEntry implements IClickable
{
    private JsonElement value;

    public ConfigEntryCustom()
    {
    }

    @Override
    public ConfigEntryType getConfigType()
    {
        return ConfigEntryType.CUSTOM;
    }

    @Override
    public int getColor()
    {
        return 0xFFAA00;
    }

    @Override
    public void fromJson(@Nonnull JsonElement o)
    {
        value = o == JsonNull.INSTANCE ? null : o;
    }

    @Nonnull
    @Override
    public JsonElement getSerializableElement()
    {
        return value == null ? JsonNull.INSTANCE : value;
    }

    @Override
    public ConfigGroup getAsGroup()
    {
        JsonElement e = getSerializableElement();
        if(e.isJsonNull())
        {
            return null;
        }
        else if(e.isJsonObject())
        {
            ConfigGroup group = new ConfigGroup();
            group.fromJson(e);
            return group;
        }
        return null;
    }

    @Override
    public String getAsString()
    {
        JsonElement e = getSerializableElement();
        return e.isJsonNull() ? ". . ." : String.valueOf(e);
    }

    @Override
    public List<String> getAsStringList()
    {
        JsonElement e = getSerializableElement();
        if(e.isJsonNull())
        {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        for(JsonElement e1 : e.getAsJsonArray())
        {
            list.add(e1.getAsString());
        }
        return list;
    }

    @Override
    public boolean getAsBoolean()
    {
        JsonElement e = getSerializableElement();
        return !e.isJsonNull() && e.getAsBoolean();
    }

    @Override
    public int getAsInt()
    {
        JsonElement e = getSerializableElement();
        return e.isJsonNull() ? 0 : e.getAsInt();
    }

    @Override
    public double getAsDouble()
    {
        JsonElement e = getSerializableElement();
        return e.isJsonNull() ? 0D : e.getAsDouble();
    }

    @Override
    public TIntList getAsIntList()
    {
        JsonElement e = getSerializableElement();
        if(e.isJsonNull())
        {
            return null;
        }
        JsonArray a = e.getAsJsonArray();
        TIntList l = new TIntArrayList(a.size());
        for(int i = 0; i < l.size(); i++)
        {
            l.set(i, a.get(i).getAsInt());
        }
        return l;
    }

    @Override
    public void onClicked(@Nonnull MouseButton button)
    {
    }

    @Override
    public ConfigEntry copy()
    {
        ConfigEntryCustom entry = new ConfigEntryCustom();
        entry.fromJson(getSerializableElement());
        return entry;
    }

    @Override
    public void writeData(ByteIOStream io, boolean extended)
    {
        super.writeData(io, extended);
        JsonElementIO.write(io, getSerializableElement());
    }

    @Override
    public void readData(ByteIOStream io, boolean extended)
    {
        super.readData(io, extended);
        fromJson(JsonElementIO.read(io));
    }
}