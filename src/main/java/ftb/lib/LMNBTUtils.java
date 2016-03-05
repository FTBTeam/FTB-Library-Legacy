package ftb.lib;

import latmod.lib.*;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.nbt.*;
import net.minecraftforge.common.util.Constants;

import java.io.File;
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
		return LMListUtils.toStringArray(tag.getKeySet());
	}
	
	public static Map<String, NBTBase> toMap(NBTTagCompound tag)
	{
		if(tag == null) return null;
		HashMap<String, NBTBase> map = new HashMap<>();
		if(tag.hasNoTags()) return map;
		
		for(String s : tag.getKeySet())
		{
			map.put(s, tag.getTag(s));
		}
		
		return map;
	}
	
	public static Set<Map.Entry<String, NBTBase>> entrySet(NBTTagCompound tag)
	{
		if(tag == null) return null;
		HashSet<Map.Entry<String, NBTBase>> l = new HashSet<>();
		if(tag.hasNoTags()) return l;
		
		for(String s : tag.getKeySet())
		{
			l.add(new AbstractMap.SimpleEntry<String, NBTBase>(s, tag.getTag(s)));
		}
		
		return l;
	}
	
	public static Exception writeTag(File f, NBTTagCompound tag)
	{
		try { CompressedStreamTools.write(tag, f); }
		catch(Exception e) { return e; }
		return null;
	}
	
	public static NBTTagCompound readTag(File f)
	{
		if(f == null || !f.exists()) return null;
		try { return CompressedStreamTools.read(f); }
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
		byte i = data.readByte();
		if(i == 0) return null;
		else if(i == 1) return new NBTTagCompound();
		try { return CompressedStreamTools.read(data, NBTSizeTracker.INFINITE); }
		catch(Exception ex) { ex.printStackTrace(); }
		return null;
	}
	
	public static void writeTag(ByteIOStream data, NBTTagCompound tag)
	{
		data.writeByte((tag == null) ? 0 : (tag.hasNoTags() ? 1 : 2));
		try { if(tag != null && !tag.hasNoTags()) CompressedStreamTools.write(tag, data); }
		catch(Exception ex) { ex.printStackTrace(); }
	}
	
	public static UUID getUUID(NBTTagCompound tag, String key)
	{
		if(tag == null || !tag.hasKey(key)) return null;
		return UUIDTypeAdapterLM.getUUID(tag.getString(key));
	}
	
	public static void setUUID(NBTTagCompound tag, String key, UUID uuid)
	{
		if(tag == null || key == null || key.isEmpty() || uuid == null) return;
		tag.setString(key, UUIDTypeAdapterLM.getString(uuid));
	}
}