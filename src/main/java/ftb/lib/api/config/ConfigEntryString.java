package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;

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
	
	public PrimitiveType getType()
	{ return PrimitiveType.STRING; }
	
	public void set(String o)
	{ value = o == null ? "" : o; }
	
	public String get()
	{ return value; }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsString()); }
	
	public final JsonElement getJson()
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