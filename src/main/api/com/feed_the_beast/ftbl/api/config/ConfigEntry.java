package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonElement;
import com.latmod.lib.annotations.IFlagContainer;
import com.latmod.lib.annotations.IInfoContainer;
import com.latmod.lib.io.Bits;
import com.latmod.lib.io.ByteIOStream;
import com.latmod.lib.util.JsonElementIO;
import com.latmod.lib.util.LMJsonUtils;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class ConfigEntry implements IInfoContainer, IFlagContainer, IJsonSerializable
{
    private byte flags;
    private String[] info;
    private ITextComponent displayName;

    ConfigEntry()
    {
    }

    public abstract ConfigEntryType getConfigType();

    @Override
    public abstract void fromJson(JsonElement o);

    @Override
    public abstract JsonElement getSerializableElement();

    public int getColor()
    {
        return 0x999999;
    }

    @Nullable
    public String getDefValueString()
    {
        return null;
    }

    @Nullable
    public String getMinValueString()
    {
        return null;
    }

    @Nullable
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

    public boolean getAsBoolean()
    {
        return false;
    }

    public int getAsInt()
    {
        return 0;
    }

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

    @Nullable
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
        return info == null ? IInfoContainer.NO_INFO : info;
    }

    @Override
    public final void setInfo(String[] s)
    {
        info = s;
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

    @Nullable
    public List<String> getVariants()
    {
        return null;
    }
}