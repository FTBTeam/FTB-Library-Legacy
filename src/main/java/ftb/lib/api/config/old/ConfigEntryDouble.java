package ftb.lib.api.config.old;

import com.google.gson.*;
import latmod.lib.*;

public class ConfigEntryDouble extends ConfigEntry
{
	public double defValue;
	private double value;
	
	public ConfigEntryDouble(String id, double d)
	{
		super(id);
		defValue = d;
		set(d);
	}
	
	public PrimitiveType getType()
	{ return PrimitiveType.DOUBLE; }
	
	public void set(double v)
	{ value = configData.getDouble(v); }
	
	public double get()
	{ return value; }
	
	public void add(double v)
	{ set(get() + v); }
	
	public final void setJson(JsonElement o)
	{ set(o.getAsDouble()); }
	
	public final JsonElement getJson()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeDouble(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readDouble()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeDouble(defValue);
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue = io.readDouble();
	}
	
	public String getAsString()
	{ return Double.toString(get()); }
	
	public int getAsInt()
	{ return (int) get(); }
	
	public double getAsDouble()
	{ return get(); }
	
	public String getDefValue()
	{ return Double.toString(defValue); }
}