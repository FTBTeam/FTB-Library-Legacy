package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;
import latmod.lib.annotations.INumberBoundsContainer;
import net.minecraft.nbt.NBTTagCompound;

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
	
	@Override
	public ConfigEntryType getConfigType()
	{ return ConfigEntryType.DOUBLE; }
	
	@Override
	public int getColor()
	{ return 0xAA5AE8; }
	
	@Override
	public void setBounds(double min, double max)
	{
		minValue = min == Double.NEGATIVE_INFINITY ? null : min;
		maxValue = max == Double.POSITIVE_INFINITY ? null : max;
	}
	
	@Override
	public double getMin()
	{ return minValue == null ? Double.NEGATIVE_INFINITY : minValue; }
	
	@Override
	public double getMax()
	{ return maxValue == null ? Double.POSITIVE_INFINITY : maxValue; }
	
	public void set(double v)
	{
		value = MathHelperLM.clamp(v, getMin(), getMax());
	}
	
	public void add(double v)
	{ set(getAsDouble() + v); }
	
	@Override
	public final void fromJson(JsonElement o)
	{ set(o.getAsDouble()); }
	
	@Override
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(getAsDouble()); }
	
	@Override
	public String getAsString()
	{ return Double.toString(getAsDouble()); }
	
	@Override
	public int getAsInt()
	{ return (int) getAsDouble(); }
	
	@Override
	public double getAsDouble()
	{ return value; }
	
	@Override
	public String getDefValueString()
	{ return Double.toString(defValue); }
	
	@Override
	public String getMinValueString()
	{
		double d = getMin();
		
		if(d != Double.NEGATIVE_INFINITY)
		{
			return LMStringUtils.formatDouble(d);
		}
		
		return null;
	}
	
	@Override
	public String getMaxValueString()
	{
		double d = getMax();
		
		if(d != Double.POSITIVE_INFINITY)
		{
			return LMStringUtils.formatDouble(d);
		}
		
		return null;
	}
	
	@Override
	public void writeToNBT(NBTTagCompound tag, boolean extended)
	{
		super.writeToNBT(tag, extended);
		
		double d = getAsDouble();
		
		if(d != 0D) tag.setDouble("V", d);
		
		if(extended)
		{
			if(defValue != 0D) tag.setDouble("D", defValue);
			
			d = getMin();
			
			if(d != Double.NEGATIVE_INFINITY) tag.setDouble("MN", d);
			
			d = getMax();
			
			if(d != Double.POSITIVE_INFINITY) tag.setDouble("MX", d);
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag, boolean extended)
	{
		super.readFromNBT(tag, extended);
		set(tag.getDouble("V"));
		
		if(extended)
		{
			defValue = tag.getDouble("D");
			setBounds(tag.hasKey("MN") ? tag.getDouble("MN") : Double.NEGATIVE_INFINITY, tag.hasKey("MX") ? tag.getDouble("MX") : Double.POSITIVE_INFINITY);
		}
	}
}