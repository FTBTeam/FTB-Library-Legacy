package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.latmod.lib.annotations.IFlagContainer;
import com.latmod.lib.annotations.IInfoContainer;
import com.latmod.lib.io.Bits;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.json.JsonElementIO;
import com.latmod.lib.json.LMJsonUtils;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import java.util.function.IntSupplier;

public abstract class ConfigEntry implements IInfoContainer, IFlagContainer, IJsonSerializable, BooleanSupplier, IntSupplier, DoubleSupplier
{
    private byte flags;
    private String[] info;
    private ITextComponent displayName;

    ConfigEntry()
    {
    }

    public abstract ConfigEntryType getConfigType();

    @Override
    public abstract void fromJson(@Nonnull JsonElement o);

    @Nonnull
    @Override
    public abstract JsonElement getSerializableElement();

    public int getColor()
    {
        return 0x999999;
    }

    public String getDefValueString()
    {
        return null;
    }

    public String getMinValueString()
    {
        return null;
    }

    public String getMaxValueString()
    {
        return null;
    }

    public ITextComponent getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(ITextComponent c)
    {
        displayName = c;
    }

    public abstract ConfigEntry copy();

    @Override
    public final String toString()
    {
        return getAsString();
    }

    public abstract String getAsString();

    @Override
    public boolean getAsBoolean()
    {
        return false;
    }

    @Override
    public int getAsInt()
    {
        return 0;
    }

    @Override
    public double getAsDouble()
    {
        return 0D;
    }

    public TIntList getAsIntList()
    {
        return TIntArrayList.wrap(new int[] {getAsInt()});
    }

    public List<String> getAsStringList()
    {
        return Collections.singletonList(getAsString());
    }

    public ConfigGroup getAsGroup()
    {
        return null;
    }

    @Override
    public final int getFlags()
    {
        return flags;
    }

    @Override
    public final void setFlags(int f)
    {
        flags = (byte) f;
    }

    @Override
    public final String[] getInfo()
    {
        return info;
    }

    @Override
    public final void setInfo(String[] s)
    {
        info = (s != null && s.length > 0) ? s : null;
    }

    public void writeData(ByteIOStream io, boolean extended)
    {
        if(extended)
        {
            io.writeByte(getFlags());
            int extraFlags = 0;
            Bits.setFlag(extraFlags, 1, displayName != null);
            Bits.setFlag(extraFlags, 2, info != null && info.length > 0);
            io.writeByte(extraFlags);

            if(displayName != null)
            {
                JsonElementIO.write(io, LMJsonUtils.serializeTextComponent(displayName));
            }

            if(info != null && info.length > 0)
            {
                io.writeShort(info.length);

                for(String s : info)
                {
                    io.writeUTF(s);
                }
            }
        }
    }

    public void readData(ByteIOStream io, boolean extended)
    {
        if(extended)
        {
            setFlags(io.readByte());
            int extraFlags = io.readByte();

            if(Bits.getFlag(extraFlags, 1))
            {
                displayName = LMJsonUtils.deserializeTextComponent(JsonElementIO.read(io));
            }
            else
            {
                displayName = null;
            }

            if(Bits.getFlag(extraFlags, 2))
            {
                int s = io.readUnsignedByte();
                info = new String[s];

                for(int i = 0; i < s; i++)
                {
                    info[i] = io.readUTF();
                }
            }
            else
            {
                info = null;
            }
        }
    }

    public List<String> getVariants()
    {
        return null;
    }
}