package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;

import java.util.*;

public class ConfigEntryStringArray extends ConfigEntry
{
	public final List<String> defValue;
	private List<String> value;
	
	public ConfigEntryStringArray(String id, List<String> def)
	{
		super(id);
		value = new ArrayList<>();
		defValue = new ArrayList<>();
		set(def);
		defValue.addAll(def);
	}
	
	public ConfigEntryStringArray(String id, String... def)
	{ this(id, (def == null || def.length == 0) ? new ArrayList<String>() : Arrays.asList(def)); }
	
	public ConfigType getConfigType()
	{ return ConfigType.STRING_ARRAY; }
	
	public int getColor()
	{ return 0xFFAA49; }
	
	public void set(List<String> o)
	{
		value.clear();
		value.addAll(o);
	}
	
	public List<String> get()
	{ return value; }
	
	public final void func_152753_a(JsonElement o)
	{
		JsonArray a = o.getAsJsonArray();
		value.clear();
		for(int i = 0; i < a.size(); i++)
			value.add(a.get(i).getAsString());
		set(LMListUtils.clone(value));
	}
	
	public final JsonElement getSerializableElement()
	{
		JsonArray a = new JsonArray();
		value = get();
		for(String aValue : value) a.add(new JsonPrimitive(aValue));
		return a;
	}
	
	public void write(ByteIOStream io)
	{
		value = get();
		io.writeShort(value.size());
		for(String aValue : value) io.writeUTF(aValue);
	}
	
	public void read(ByteIOStream io)
	{
		value.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
			value.add(io.readUTF());
		set(LMListUtils.clone(value));
	}
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeShort(defValue.size());
		for(String aDefValue : defValue) io.writeUTF(aDefValue);
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
			defValue.add(io.readUTF());
	}
	
	public String getAsString()
	{ return get().toString(); }
	
	public String[] getAsStringArray()
	{ return LMListUtils.toStringArray(get()); }
	
	public boolean getAsBoolean()
	{ return !get().isEmpty(); }
	
	public int[] getAsIntArray()
	{
		value = get();
		int[] ai = new int[value.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = Integer.parseInt(value.get(i));
		return ai;
	}
	
	public double[] getAsDoubleArray()
	{
		value = get();
		double[] ai = new double[value.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = Double.parseDouble(value.get(i));
		return ai;
	}
	
	public String getDefValueString()
	{ return defValue.toString(); }
}