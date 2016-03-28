package ftb.lib.api.config;

import com.google.gson.*;
import net.minecraft.nbt.*;

import java.util.LinkedHashMap;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigEntryEnum<E extends Enum<E>> extends ConfigEntry
{
	private final LinkedHashMap<String, E> enumMap;
	private E value, defValue;
	
	public ConfigEntryEnum(String id, E[] val, E def, boolean addNull)
	{
		super(id);
		
		enumMap = new LinkedHashMap<>();
		
		for(E e : val)
		{
			enumMap.put(getName(e), e);
		}
		
		if(addNull)
		{
			enumMap.put("-", null);
		}
		
		value = def;
		defValue = def;
	}
	
	private ConfigEntryEnum(String id, LinkedHashMap<String, E> map, E def)
	{
		super(id);
		enumMap = map;
		defValue = def;
		value = def;
	}
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.ENUM; }
	
	public final ConfigEntry simpleCopy()
	{ return new ConfigEntryEnum(getID(), new LinkedHashMap(enumMap), defValue); }
	
	public void setEnum(E v)
	{ value = v; }
	
	public E get()
	{ return value; }
	
	public static String getName(Enum<?> e)
	{ return e == null ? "-" : e.name().toLowerCase(); }
	
	public final int getColor()
	{ return 0x0094FF; }
	
	public final String getDefValueString()
	{ return getName(defValue); }
	
	public final void fromJson(JsonElement json)
	{ setEnum(enumMap.get(json.getAsString())); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(getName(get())); }
	
	public final NBTBase serializeNBT()
	{ return new NBTTagString(getName(get())); }
	
	public final void deserializeNBT(NBTBase nbt)
	{ setEnum(enumMap.get(((NBTTagString) nbt).getString())); }
	
	public final void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("D", getName(defValue));
	}
	
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		defValue = enumMap.get(nbt.getString("D"));
	}
	
	public final String getAsString()
	{ return getName(get()); }
	
	public final int getAsInt()
	{
		int i = 0;
		value = get();
		for(E e : enumMap.values())
		{
			if(value == e)
			{
				return i;
			}
			i++;
		}
		
		return -1;
	}
	
	public boolean getAsBoolean()
	{ return get() != null; }
}