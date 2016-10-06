package com.feed_the_beast.ftbl.lib.config;

import com.feed_the_beast.ftbl.api.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.lib.util.LMJsonUtils;
import com.feed_the_beast.ftbl.lib.util.LMNetUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by LatvianModder on 24.09.2016.
 */
public class PropertyTextComponentList extends PropertyBase
{
    public static final String ID = "text_component_list";

    @ConfigValueProvider(ID)
    public static final IConfigValueProvider PROVIDER = () -> new PropertyTextComponentList(Collections.emptyList());

    private List<ITextComponent> value;

    public PropertyTextComponentList(Collection<ITextComponent> l)
    {
        value = new ArrayList<>(l);
    }

    @Override
    public String getID()
    {
        return ID;
    }

    public List<ITextComponent> getText()
    {
        return value;
    }

    public void setText(List<ITextComponent> l)
    {
        value = l;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return getText();
    }

    @Override
    public String getString()
    {
        return getText().toString();
    }

    @Override
    public boolean getBoolean()
    {
        return !getText().isEmpty();
    }

    @Override
    public int getInt()
    {
        return getText().size();
    }

    @Override
    public IConfigValue copy()
    {
        return new PropertyTextComponentList(getText());
    }

    @Override
    public NBTBase serializeNBT()
    {
        NBTTagList list = new NBTTagList();
        getText().forEach(c -> list.appendTag(new NBTTagString(ITextComponent.Serializer.componentToJson(c))));
        return list;
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        value.clear();
        NBTTagList list = (NBTTagList) nbt;

        for(int i = 0; i < list.tagCount(); i++)
        {
            value.add(ITextComponent.Serializer.jsonToComponent(list.getStringTagAt(i)));
        }

        setText(value);
    }

    @Override
    public void fromJson(JsonElement o)
    {
        value.clear();

        if(o.isJsonArray())
        {
            o.getAsJsonArray().forEach(e ->
            {
                ITextComponent t = LMJsonUtils.deserializeTextComponent(e);

                if(t != null)
                {
                    value.add(t);
                }
            });
        }

        setText(value);
    }

    @Override
    public JsonElement getSerializableElement()
    {
        JsonArray a = new JsonArray();
        getText().forEach(t -> a.add(LMJsonUtils.serializeTextComponent(t)));
        return a;
    }

    @Override
    public void writeData(ByteBuf data, boolean extended)
    {
        data.writeShort(value.size());
        getText().forEach(c -> LMNetUtils.writeTextComponent(data, c));
    }

    @Override
    public void readData(ByteBuf data, boolean extended)
    {
        int s = data.readUnsignedShort();
        value.clear();

        while(--s >= 0)
        {
            value.add(LMNetUtils.readTextComponent(data));
        }

        setText(value);
    }
}
