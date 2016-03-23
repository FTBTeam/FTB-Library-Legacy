package ftb.lib;

import ftb.lib.mod.FTBLibMod;
import latmod.lib.*;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;

import java.io.*;
import java.util.*;

@SuppressWarnings("all")
public class LMNBTUtils
{
	public static final byte END = Constants.NBT.TAG_END;
	public static final byte BYTE = Constants.NBT.TAG_BYTE;
	public static final byte SHORT = Constants.NBT.TAG_SHORT;
	public static final byte INT = Constants.NBT.TAG_INT;
	public static final byte LONG = Constants.NBT.TAG_LONG;
	public static final byte FLOAT = Constants.NBT.TAG_FLOAT;
	public static final byte DOUBLE = Constants.NBT.TAG_DOUBLE;
	public static final byte BYTE_ARRAY = Constants.NBT.TAG_BYTE_ARRAY;
	public static final byte STRING = Constants.NBT.TAG_STRING;
	public static final byte LIST = Constants.NBT.TAG_LIST;
	public static final byte MAP = Constants.NBT.TAG_COMPOUND;
	public static final byte INT_ARRAY = Constants.NBT.TAG_INT_ARRAY;
	
	public static String[] getMapKeys(NBTTagCompound tag)
	{
		if(tag == null || tag.hasNoTags()) return new String[0];
		return LMListUtils.toStringArray(tag.func_150296_c());
	}
	
	public static Map<String, NBTBase> toMap(NBTTagCompound tag)
	{
		Map<String, NBTBase> map = new HashMap<String, NBTBase>();
		for(Object s : tag.func_150296_c())
			map.put(s.toString(), tag.getTag(s.toString()));
		return map;
	}
	
	public static <E extends NBTBase> Map<String, E> toMapWithType(NBTTagCompound tag)
	{
		HashMap<String, E> map = new HashMap<String, E>();
		if(tag == null || tag.hasNoTags()) return map;
		for(Object s : tag.func_150296_c())
			map.put(s.toString(), (E) tag.getTag(s.toString()));
		return map;
	}
	
	public static Exception writeMap(File f, NBTTagCompound tag)
	{
		try { CompressedStreamTools.write(tag, f); }
		catch(Exception e) { return e; }
		return null;
	}
	
	public static NBTTagCompound readMap(File f)
	{
		if(f == null || !f.exists()) return null;
		try { return CompressedStreamTools.read(f); }
		catch(Exception e)
		{
			e.printStackTrace();
			FTBLibMod.logger.info("Possibly corrupted / old file. Trying the old method");
			
			try
			{
				FileInputStream is = new FileInputStream(f);
				byte[] b = new byte[is.available()];
				is.read(b);
				is.close();
				return CompressedStreamTools.func_152457_a(b, NBTSizeTracker.field_152451_a);
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}
		}
		return null;
	}
	
	public static boolean areTagsEqual(NBTTagCompound tag1, NBTTagCompound tag2)
	{
		if(tag1 == null && tag2 == null) return true;
		if(tag1 != null && tag2 == null) return false;
		if(tag1 == null && tag2 != null) return false;
		return tag1.equals(tag2);
	}
	
	public static void toStringList(List<String> l, NBTTagList tag)
	{
		l.clear();
		for(int i = 0; i < tag.tagCount(); i++)
			l.add(tag.getStringTagAt(i));
	}
	
	public static List<String> toStringList(NBTTagList tag)
	{
		ArrayList<String> l = new ArrayList<String>();
		toStringList(l, tag);
		return l;
	}
	
	public static NBTTagList fromStringList(List<String> l)
	{
		NBTTagList tag = new NBTTagList();
		for(int i = 0; i < l.size(); i++)
			tag.appendTag(new NBTTagString(l.get(i)));
		return tag;
	}
	
	public static NBTTagCompound readTag(ByteIOStream data)
	{
		try
		{
			byte[] abyte = data.readByteArray(ByteCount.INT);
			
			if(abyte == null) return null;
			else if(abyte.length == 0) return new NBTTagCompound();
			else return CompressedStreamTools.func_152457_a(abyte, NBTSizeTracker.field_152451_a);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
		
		return null;
	}
	
	public static void writeTag(ByteIOStream data, NBTTagCompound tag)
	{
		try
		{
			byte[] abyte;
			
			if(tag == null) abyte = null;
			else if(tag.hasNoTags()) abyte = new byte[0];
			else abyte = CompressedStreamTools.compress(tag);
			
			data.writeByteArray(abyte, ByteCount.INT);
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
}