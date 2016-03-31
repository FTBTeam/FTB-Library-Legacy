package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.ByteIOStream;

public class ConfigEntryString extends ConfigEntry
{
	public String defValue;
	private String value;
	
	public ConfigEntryString(String id, String def)
	{
		super(id);
		set(def);
		defValue = def == null ? "" : def;
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.STRING; }
	
	public int getColor()
	{ return 0xFFAA49; }
	
	public void set(String o)
	{ value = o == null ? "" : o; }
	
	public String get()
	{ return value; }
	
	public final void func_152753_a(JsonElement o)
	{ set(o.getAsString()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeUTF(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readUTF()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeUTF(defValue);
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue = io.readUTF();
	}
	
	public String getAsString()
	{ return get(); }
	
	public boolean getAsBoolean()
	{ return get().equals("true"); }
	
	public int getAsInt()
	{ return Integer.parseInt(get()); }
	
	public double getAsDouble()
	{ return Double.parseDouble(get()); }
	
	public String getDefValueString()
	{ return defValue; }
}