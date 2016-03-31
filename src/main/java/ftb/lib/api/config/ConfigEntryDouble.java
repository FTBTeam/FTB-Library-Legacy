package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;
import latmod.lib.annotations.INumberBoundsContainer;

public class ConfigEntryDouble extends ConfigEntry implements INumberBoundsContainer
{
	public double defValue;
	private double value;
	private Double minValue, maxValue;
	
	public ConfigEntryDouble(String id, double d)
	{
		super(id);
		defValue = d;
		set(d);
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.DOUBLE; }
	
	public int getColor()
	{ return 0xAA5AE8; }
	
	public void setBounds(double min, double max)
	{
		minValue = min == Double.NEGATIVE_INFINITY ? null : min;
		maxValue = max == Double.POSITIVE_INFINITY ? null : max;
	}
	
	public double getMin()
	{ return minValue == null ? Double.NEGATIVE_INFINITY : minValue; }
	
	public double getMax()
	{ return maxValue == null ? Double.POSITIVE_INFINITY : maxValue; }
	
	public void set(double v)
	{
		value = MathHelperLM.clamp(v, getMin(), getMax());
	}
	
	public double get()
	{ return value; }
	
	public void add(double v)
	{ set(get() + v); }
	
	public final void func_152753_a(JsonElement o)
	{ set(o.getAsDouble()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeDouble(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readDouble()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeDouble(defValue);
		io.writeDouble(getMin());
		io.writeDouble(getMax());
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue = io.readDouble();
		double min = io.readDouble();
		double max = io.readDouble();
		setBounds(min, max);
	}
	
	public String getAsString()
	{ return Double.toString(get()); }
	
	public int getAsInt()
	{ return (int) get(); }
	
	public double getAsDouble()
	{ return get(); }
	
	public String getDefValueString()
	{ return Double.toString(defValue); }
	
	public String getMinValueString()
	{
		double d = getMin();
		
		if(d != Double.NEGATIVE_INFINITY)
		{
			return LMStringUtils.formatDouble(d);
		}
		
		return null;
	}
	
	public String getMaxValueString()
	{
		double d = getMax();
		
		if(d != Double.POSITIVE_INFINITY)
		{
			return LMStringUtils.formatDouble(d);
		}
		
		return null;
	}
}