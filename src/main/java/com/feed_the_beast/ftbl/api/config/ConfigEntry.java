package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.api.net.MessageLM;
import com.feed_the_beast.ftbl.util.JsonHelper;
import com.google.gson.JsonElement;
import com.latmod.lib.annotations.IFlagContainer;
import com.latmod.lib.annotations.IInfoContainer;
import com.latmod.lib.io.Bits;
import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;
import io.netty.buffer.ByteBuf;
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
    private int flags;
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
        flags = f;
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

    public void writeData(ByteBuf io, boolean extended)
    {
        if(extended)
        {
            flags = getFlags();

            if(displayName != null)
            {
                flags = Bits.setFlag(flags, 512, true);
            }

            if(info != null && info.length > 0)
            {
                flags = Bits.setFlag(flags, 1024, true);
            }

            io.writeShort(flags);

            if(displayName != null)
            {
                MessageLM.writeJsonElement(io, JsonHelper.serializeICC(displayName));
            }

            if(info != null && info.length > 0)
            {
                io.writeShort(info.length);

                for(String s : info)
                {
                    MessageLM.writeString(io, s);
                }
            }
        }
    }

    public void readData(ByteBuf io, boolean extended)
    {
        if(extended)
        {
            flags = io.readUnsignedShort();
            setFlags(flags & 0xFF);
            displayName = null;
            info = null;

            if(Bits.getFlag(flags, 512))
            {
                displayName = JsonHelper.deserializeICC(MessageLM.readJsonElement(io));
            }

            if(Bits.getFlag(flags, 1024))
            {
                int s = io.readUnsignedByte();

                info = new String[s];

                for(int i = 0; i < s; i++)
                {
                    info[i] = MessageLM.readString(io);
                }
            }
        }
    }

    public List<String> getVariants()
    {
        return null;
    }
}