package com.feed_the_beast.ftblib.lib.util;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public class NBTUtils
{
	public static void writeTag(File f, NBTTagCompound tag)
	{
		try
		{
			CompressedStreamTools.write(tag, FileUtils.newFile(f));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	@Nullable
	public static NBTTagCompound readTag(File f)
	{
		if (!f.exists())
		{
			return null;
		}

		try
		{
			return CompressedStreamTools.read(f);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static boolean areTagsEqual(@Nullable NBTTagCompound tag1, @Nullable NBTTagCompound tag2)
	{
		return tag1 == tag2 || (tag1 != null && tag2 != null && tag1.equals(tag2));
	}

	public static void toStringList(List<String> l, NBTTagList tag)
	{
		l.clear();
		for (int i = 0; i < tag.tagCount(); i++)
		{
			l.add(tag.getStringTagAt(i));
		}
	}
}