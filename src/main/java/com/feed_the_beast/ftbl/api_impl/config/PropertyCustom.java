package com.feed_the_beast.ftbl.api_impl.config;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nullable;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Created by LatvianModder on 14.09.2016.
 */
public abstract class PropertyCustom extends PropertyBase
{
    public static final String ID = "custom";

    @Override
    public String getID()
    {
        return ID;
    }

    @Nullable
    @Override
    public Object getValue()
    {
        return null;
    }

    @Override
    public String getString()
    {
        return null;
    }

    @Override
    public boolean getBoolean()
    {
        return false;
    }

    @Override
    public int getInt()
    {
        return 0;
    }

    @Override
    public void writeData(DataOutput data, boolean extended) throws IOException
    {
        NBTTagCompound nbt = null;

        NBTBase b = serializeNBT();

        if(b != null)
        {
            nbt = new NBTTagCompound();
            nbt.setTag("D", b);
        }

        data.writeByte(nbt == null ? 0 : 1);

        if(nbt != null)
        {
            CompressedStreamTools.write(nbt, data);
        }
    }

    @Override
    public void readData(DataInput data, boolean extended) throws IOException
    {
        if(data.readByte() != 0)
        {
            NBTTagCompound nbt = CompressedStreamTools.read(data, NBTSizeTracker.INFINITE);
            deserializeNBT(nbt.getTag("D"));
        }
    }
}
