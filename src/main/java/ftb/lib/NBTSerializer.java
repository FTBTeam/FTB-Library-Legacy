package ftb.lib;

import com.google.gson.*;
import latmod.lib.LMMapUtils;
import net.minecraft.nbt.*;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created by LatvianModder on 10.01.2016.
 */
public class NBTSerializer implements JsonSerializer<NBTBase>, JsonDeserializer<NBTBase>
{
	public static final NBTSerializer instance = new NBTSerializer();
	private static final HashMap<Byte, String> types = new HashMap<>();
	private static final HashMap<String, Byte> typesInv = new HashMap<>();
	
	static
	{
		types.put(LMNBTUtils.BYTE, "NB");
		types.put(LMNBTUtils.SHORT, "NS");
		types.put(LMNBTUtils.INT, "NI");
		types.put(LMNBTUtils.LONG, "NL");
		types.put(LMNBTUtils.FLOAT, "NF");
		types.put(LMNBTUtils.DOUBLE, "ND");
		types.put(LMNBTUtils.BYTE_ARRAY, "BA");
		types.put(LMNBTUtils.STRING, "ST");
		types.put(LMNBTUtils.LIST, "LT");
		types.put(LMNBTUtils.MAP, "MP");
		types.put(LMNBTUtils.INT_ARRAY, "IA");
		typesInv.putAll(LMMapUtils.inverse(types));
	}
	
	public JsonElement serialize(NBTBase src, Type typeOfSrc, JsonSerializationContext context)
	{
		if(src instanceof NBTTagCompound)
		{
			JsonObject o = new JsonObject();
			for(Map.Entry<String, NBTBase> e : LMNBTUtils.toMap((NBTTagCompound) src).entrySet())
			{
				
				o.add(e.getKey(), serialize(e.getValue(), e.getValue().getClass(), context));
			}
			return o;
		}
		else if(src instanceof NBTTagList)
		{
			JsonArray a = new JsonArray();
			
			NBTTagList list = (NBTTagList) src;
			byte type = (byte) list.func_150303_d();
			
			a.add(get(type, null));
			
			for(int i = 0; i < list.tagCount(); i++)
			{
				if(type == LMNBTUtils.STRING) a.add(new JsonPrimitive(list.getStringTagAt(i)));
				else if(type == LMNBTUtils.INT_ARRAY)
					a.add(serialize(new NBTTagIntArray(list.func_150306_c(i)), NBTTagIntArray.class, context));
				else if(type == LMNBTUtils.FLOAT) a.add(new JsonPrimitive(list.func_150308_e(i)));
				else if(type == LMNBTUtils.DOUBLE) a.add(new JsonPrimitive(list.func_150309_d(i)));
				else if(type == LMNBTUtils.MAP)
					a.add(serialize(list.getCompoundTagAt(i), NBTTagCompound.class, context));
			}
			
			return a;
		}
		else if(src instanceof NBTTagString)
		{
			return get(LMNBTUtils.STRING, ((NBTTagString) src).func_150285_a_());
		}
		else if(src instanceof NBTBase.NBTPrimitive)
		{
			return get(src.getId(), src.toString());
		}
		else if(src instanceof NBTTagIntArray)
		{
			JsonArray a = new JsonArray();
			a.add(get(LMNBTUtils.INT, null));
			
			int[] ai = ((NBTTagIntArray) src).func_150302_c();
			if(ai.length == 0) return a;
			
			for(int i = 0; i < ai.length; i++)
				a.add(new JsonPrimitive(ai[i]));
			return a;
		}
		else if(src instanceof NBTTagByteArray)
		{
			JsonArray a = new JsonArray();
			a.add(get(LMNBTUtils.BYTE, null));
			
			byte[] ai = ((NBTTagByteArray) src).func_150292_c();
			if(ai.length == 0) return a;
			
			for(int i = 0; i < ai.length; i++)
				a.add(new JsonPrimitive(ai[i]));
			return a;
		}
		
		return null;
	}
	
	private static String getNumber(NBTBase.NBTPrimitive p)
	{
		return "";
	}
	
	private static JsonPrimitive get(byte id, Object extra)
	{
		StringBuilder sb = new StringBuilder();
		sb.append(types.get(id));
		
		if(extra != null)
		{
			sb.append('_');
			sb.append(extra);
		}
		
		return new JsonPrimitive(sb.toString());
	}
	
	public NBTBase deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
	{
		return null;
	}
}
