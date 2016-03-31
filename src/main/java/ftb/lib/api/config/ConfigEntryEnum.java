package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.ByteIOStream;

import java.util.LinkedHashMap;

public class ConfigEntryEnum<E extends Enum<E>> extends ConfigEntry implements IClickableConfigEntry // EnumTypeAdapterFactory
{
	private final LinkedHashMap<String, E> enumMap;
	private E value;
	public final E defValue;
	
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
		
		set(def);
		defValue = def;
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.ENUM; }
	
	public int getColor()
	{ return 0x0094FF; }
	
	public void set(Object o)
	{ value = (E) o; }
	
	public E get()
	{ return value; }
	
	public static String getName(Enum<?> e)
	{ return e == null ? "-" : e.name().toLowerCase(); }
	
	private E fromString(String s)
	{ return enumMap.get(s.toLowerCase()); }
	
	public final void func_152753_a(JsonElement o)
	{ set(fromString(o.getAsString())); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(getName(get())); }
	
	public void write(ByteIOStream io)
	{ io.writeUTF(getName(get())); }
	
	public void read(ByteIOStream io)
	{ fromString(io.readUTF()); }
	
	public void writeExtended(ByteIOStream io)
	{
		io.writeByte(enumMap.size());
		for(E e : enumMap.values())
			io.writeUTF(getName(e));
		io.writeByte(getIndex());
		io.writeByte(getDefaultIndex());
	}
	
	public void onClicked()
	{
		set(getFromIndex((getIndex() + 1) % enumMap.size()));
	}
	
	public String getAsString()
	{ return getName(get()); }
	
	public boolean getAsBoolean()
	{ return get() != null; }
	
	private E getFromIndex(int index)
	{
		if(index < 0 || index >= enumMap.size())
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		
		int idx0 = 0;
		for(E e : enumMap.values())
		{
			if(index == idx0) return e;
			idx0++;
		}
		
		return null;
	}
	
	private int getIndex()
	{
		int idx0 = 0;
		E e0 = get();
		for(E e : enumMap.values())
		{
			if(e == e0) return idx0;
			idx0++;
		}
		
		return -1;
	}
	
	private int getDefaultIndex()
	{
		int idx0 = 0;
		for(E e : enumMap.values())
		{
			if(e == defValue) return idx0;
			idx0++;
		}
		
		return -1;
	}
	
	public int getAsInt()
	{ return enumMap.size(); }
	
	public String getDefValueString()
	{ return getName(defValue); }
}