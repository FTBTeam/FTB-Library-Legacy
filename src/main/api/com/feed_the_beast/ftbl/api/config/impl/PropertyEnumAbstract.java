package com.feed_the_beast.ftbl.api.config.impl;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.latmod.lib.EnumNameMap;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by LatvianModder on 12.09.2016.
 */
public abstract class PropertyEnumAbstract<E extends Enum<E>> extends PropertyBase
{
    public static final ResourceLocation ID = new ResourceLocation(FTBLibFinals.MOD_ID, "enum");

    @Override
    public ResourceLocation getID()
    {
        return ID;
    }

    public abstract EnumNameMap<E> getNameMap();

    @Nullable
    public abstract E get();

    public abstract void set(@Nullable E e);

    @Override
    public Object getValue()
    {
        return get();
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        if(extended)
        {
            data.writeShort(getNameMap().size);

            for(String s : getNameMap().getKeys())
            {
                data.writeUTF(s);
            }
        }

        data.writeShort(getNameMap().getIndex(getValue()));
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        if(extended)
        {
            throw new IOException("Reading extended PropertyEnum data is not supported!");
        }

        set(getNameMap().getFromIndex(data.readShort() & 0xFFFF));
    }

    @Override
    public String getString()
    {
        return EnumNameMap.getEnumName((E) getValue());
    }

    @Override
    public boolean getBoolean()
    {
        return getValue() != null;
    }

    @Override
    public int getInt()
    {
        return getNameMap().getIndex(getValue());
    }

    @Override
    public IConfigValue copy()
    {
        //Good idea?
        return new PropertyEnum<E>(getNameMap(), (E) getValue());
    }

    @Override
    public int getColor()
    {
        return 0x0094FF;
    }

    @Override
    public void fromJson(JsonElement json)
    {
        set(getNameMap().get(json.getAsString()));
    }

    @Override
    public JsonElement getSerializableElement()
    {
        return new JsonPrimitive(getString());
    }

    @Override
    public NBTBase serializeNBT()
    {
        return new NBTTagString(getString());
    }

    @Override
    public void deserializeNBT(NBTBase nbt)
    {
        set(getNameMap().get(((NBTTagString) nbt).getString()));
    }
}