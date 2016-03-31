package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;
import latmod.lib.annotations.INumberBoundsContainer;

public class ConfigEntryInt extends ConfigEntry implements INumberBoundsContainer
{
	public int defValue;
	private int value;
	private Integer minValue, maxValue;
	
	public ConfigEntryInt(String id, int def)
	{
		super(id);
		defValue = def;
		set(def);
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.INT; }
	
	public int getColor()
	{ return 0xAA5AE8; }
	
	public void setBounds(double min, double max)
	{
		minValue = min == Double.NEGATIVE_INFINITY ? null : (int) min;
		maxValue = max == Double.POSITIVE_INFINITY ? null : (int) max;
	}
	
	public double getMin()
	{ return minValue == null ? Double.NEGATIVE_INFINITY : minValue.doubleValue(); }
	
	public double getMax()
	{ return maxValue == null ? Double.POSITIVE_INFINITY : maxValue.doubleValue(); }
	
	public void set(int v)
	{
		value = MathHelperLM.clampInt(v, (int) getMin(), (int) getMax());
	}
	
	public int get()
	{ return value; }
	
	public void add(int i)
	{ set(get() + i); }
	
	public final void func_152753_a(JsonElement o)
	{ set((o == null || o.isJsonNull()) ? defValue : o.getAsInt()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeInt(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readInt()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeInt(defValue);
		io.writeDouble(getMin());
		io.writeDouble(getMax());
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue = io.readInt();
		double min = io.readDouble();
		double max = io.readDouble();
		setBounds(min, max);
	}
	
	public String getAsString()
	{ return Integer.toString(get()); }
	
	public boolean getAsBoolean()
	{ return get() == 1; }
	
	public int getAsInt()
	{ return get(); }
	
	public double getAsDouble()
	{ return get(); }
	
	public String getDefValueString()
	{ return Integer.toString(defValue); }
	
	public String getMinValueString()
	{
		double d = getMin();
		
		if(d != Double.NEGATIVE_INFINITY)
		{
			return Integer.toString((int) d);
		}
		
		return null;
	}
	
	public String getMaxValueString()
	{
		double d = getMax();
		
		if(d != Double.POSITIVE_INFINITY)
		{
			return Integer.toString((int) d);
		}
		
		return null;
	}
}