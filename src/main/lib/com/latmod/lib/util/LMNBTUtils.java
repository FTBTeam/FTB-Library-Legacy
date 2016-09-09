package com.latmod.lib.util;

import com.latmod.lib.io.ByteIOStream;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LMNBTUtils
{
    public static String[] getMapKeys(NBTTagCompound tag)
    {
        if(tag == null || tag.hasNoTags())
        {
            return new String[0];
        }
        return LMListUtils.toStringArray(tag.getKeySet());
    }

    public static void writeTag(File f, NBTTagCompound tag)
    {
        try
        {
            CompressedStreamTools.write(tag, LMFileUtils.newFile(f));
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static NBTTagCompound readTag(File f)
    {
        if(!f.exists())
        {
            return null;
        }

        try
        {
            return CompressedStreamTools.read(f);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean areTagsEqual(NBTTagCompound tag1, NBTTagCompound tag2)
    {
        return tag1 == tag2 || (tag1 != null && tag2 != null && tag1.equals(tag2));
    }

    public static void toStringList(List<String> l, NBTTagList tag)
    {
        l.clear();
        for(int i = 0; i < tag.tagCount(); i++)
        {
            l.add(tag.getStringTagAt(i));
        }
    }

    public static List<String> toStringList(NBTTagList tag)
    {
        ArrayList<String> l = new ArrayList<>();
        toStringList(l, tag);
        return l;
    }

    public static NBTTagList fromStringList(List<String> l)
    {
        NBTTagList tag = new NBTTagList();
        for(String s : l)
        {
            tag.appendTag(new NBTTagString(s));
        }
        return tag;
    }

    public static NBTTagCompound readTag(ByteIOStream data)
    {
        byte i = data.readByte();
        if(i == 0)
        {
            return null;
        }
        else if(i == 1)
        {
            return new NBTTagCompound();
        }
        try
        {
            return CompressedStreamTools.read(data, NBTSizeTracker.INFINITE);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }

    public static void writeTag(ByteIOStream data, NBTTagCompound tag)
    {
        data.writeByte((tag == null) ? 0 : (tag.hasNoTags() ? 1 : 2));
        try
        {
            if(tag != null && !tag.hasNoTags())
            {
                CompressedStreamTools.write(tag, data);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public static UUID getUUID(NBTTagCompound tag, String key, boolean string)
    {
        if(tag == null)
        {
            return null;
        }
        else if(string)
        {
            return tag.hasKey(key) ? LMStringUtils.fromString(tag.getString(key)) : null;
        }
        else
        {
            long msb = tag.getLong(key + 'M');
            long lsb = tag.getLong(key + 'L');
            return (msb == 0L && lsb == 0L) ? null : new UUID(msb, lsb);
        }
    }

    public static void setUUID(NBTTagCompound tag, String key, UUID uuid, boolean string)
    {
        if(tag == null || key == null || key.isEmpty() || uuid == null)
        {
            return;
        }
        if(string)
        {
            tag.setString(key, LMStringUtils.fromUUID(uuid));
        }
        else
        {
            tag.setLong(key + 'M', uuid.getMostSignificantBits());
            tag.setLong(key + 'L', uuid.getLeastSignificantBits());
        }
    }
}