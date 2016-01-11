package ftb.lib;

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
	
	public static String[] getMapKeysA(NBTTagCompound tag)
	{
		if(tag == null || tag.hasNoTags()) return new String[0];
		return (String[]) tag.func_150296_c().toArray(new String[0]);
	}
	
	public static List<String> getMapKeys(NBTTagCompound tag)
	{
		ArrayList<String> list = new ArrayList<String>();
		if(tag == null || tag.hasNoTags()) return list;
		list.addAll(tag.func_150296_c());
		return list;
	}
	
	public static Map<String, NBTBase> toMap(NBTTagCompound tag)
	{
		Map<String, NBTBase> map = new HashMap<String, NBTBase>();
		List<String> keys = getMapKeys(tag);
		for(int i = 0; i < keys.size(); i++)
		{
			String s = keys.get(i);
			map.put(s, tag.getTag(s));
		}
		return map;
	}
	
	public static <E extends NBTBase> Map<String, E> toMapWithType(NBTTagCompound tag)
	{
		HashMap<String, E> map = new HashMap<String, E>();
		if(tag == null || tag.hasNoTags()) return map;
		
		List<String> keys = getMapKeys(tag);
		
		for(int i = 0; i < keys.size(); i++)
		{
			String s = keys.get(i);
			map.put(s, (E) tag.getTag(s));
		}
		
		return map;
	}
	
	public static void writeMap(OutputStream os, NBTTagCompound tag) throws Exception
	{
		byte[] b = CompressedStreamTools.compress(tag);
		os.write(b);
		os.flush();
		os.close();
	}
	
	public static Exception writeMap(File f, NBTTagCompound tag)
	{
		try { writeMap(new FileOutputStream(LMFileUtils.newFile(f)), tag); }
		catch(Exception e) { return e; }
		return null;
	}
	
	public static NBTTagCompound readMap(InputStream is) throws Exception
	{
		byte[] b = new byte[is.available()];
		is.read(b);
		is.close();
		return CompressedStreamTools.func_152457_a(b, NBTSizeTracker.field_152451_a);
	}
	
	public static NBTTagCompound readMap(File f)
	{
		if(f == null || !f.exists()) return null;
		try { return readMap(new FileInputStream(f)); }
		catch(Exception e) { e.printStackTrace(); }
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