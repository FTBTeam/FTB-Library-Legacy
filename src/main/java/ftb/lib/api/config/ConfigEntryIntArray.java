package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;

public class ConfigEntryIntArray extends ConfigEntry
{
	public final IntList defValue;
	private IntList value;
	
	public ConfigEntryIntArray(String id, IntList def)
	{
		super(id);
		value = new IntList();
		set(def);
		defValue = def == null ? new IntList() : def.copy();
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.INT_ARRAY; }
	
	public int getColor()
	{ return 0xAA5AE8; }
	
	public ConfigEntryIntArray(String id, int[] def)
	{ this(id, IntList.asList(def)); }
	
	public void set(IntList l)
	{
		value.clear();
		value.addAll(l);
	}
	
	public IntList get()
	{ return value; }
	
	public final void func_152753_a(JsonElement o)
	{
		JsonArray a = o.getAsJsonArray();
		value.clear();
		for(int i = 0; i < a.size(); i++)
			value.add(a.get(i).getAsInt());
		set(value.copy());
	}
	
	public final JsonElement getSerializableElement()
	{
		JsonArray a = new JsonArray();
		value = get();
		for(int i = 0; i < value.size(); i++)
			a.add(new JsonPrimitive(value.get(i)));
		return a;
	}
	
	public void write(ByteIOStream io)
	{
		value = get();
		io.writeShort(value.size());
		for(int i = 0; i < value.size(); i++)
			io.writeInt(value.get(i));
	}
	
	public void read(ByteIOStream io)
	{
		value.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
			value.add(io.readInt());
		set(value.copy());
	}
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeShort(defValue.size());
		for(int i = 0; i < defValue.size(); i++)
			io.writeInt(defValue.get(i));
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
			defValue.add(io.readInt());
	}
	
	public String getAsString()
	{ return get().toString(); }
	
	public String[] getAsStringArray()
	{
		value = get();
		String[] s = new String[value.size()];
		for(int i = 0; i < s.length; i++)
			s[i] = Integer.toString(value.get(i));
		return s;
	}
	
	public int[] getAsIntArray()
	{ return get().toArray(); }
	
	public double[] getAsDoubleArray()
	{
		int[] a = getAsIntArray();
		if(a == null) return null;
		double[] a1 = new double[a.length];
		for(int i = 0; i < a1.length; i++)
			a1[i] = a[i];
		return a1;
	}
	
	public String getDefValueString()
	{ return defValue.toString(); }
}