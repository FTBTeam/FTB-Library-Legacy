package ftb.lib.api.config.old;

import com.google.gson.*;
import latmod.lib.*;

public class ConfigEntryInt extends ConfigEntry
{
	public int defValue;
	private int value;
	
	public ConfigEntryInt(String id, int def)
	{
		super(id);
		defValue = def;
		set(def);
	}
	
	public PrimitiveType getType()
	{ return PrimitiveType.INT; }
	
	public void set(int v)
	{ value = (int) configData.getDouble(v); }
	
	public int get()
	{ return value; }
	
	public void add(int i)
	{ set(get() + i); }
	
	public final void setJson(JsonElement o)
	{ set((o == null || o.isJsonNull()) ? defValue : o.getAsInt()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeInt(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readInt()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeInt(defValue);
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue = io.readInt();
	}
	
	public String getAsString()
	{ return Integer.toString(get()); }
	
	public boolean getAsBoolean()
	{ return get() == 1; }
	
	public int getAsInt()
	{ return get(); }
	
	public double getAsDouble()
	{ return get(); }
	
	public String getDefValue()
	{ return Integer.toString(defValue); }
}