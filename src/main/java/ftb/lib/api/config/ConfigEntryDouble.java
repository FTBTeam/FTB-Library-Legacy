package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.LMStringUtils;
import latmod.lib.annotations.INumberBoundsContainer;
import net.minecraft.nbt.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigEntryDouble extends ConfigEntry implements INumberBoundsContainer
{
	private double value;
	public double defValue;
	private Double minValue;
	private Double maxValue;
	
	public ConfigEntryDouble(String id, double def)
	{
		super(id);
		defValue = def;
		value = def;
	}
	
	public void setBounds(double min, double max)
	{
		minValue = (min == Double.NEGATIVE_INFINITY) ? null : min;
		maxValue = (max == Double.POSITIVE_INFINITY) ? null : max;
	}
	
	public double getMin()
	{ return minValue == null ? Double.NEGATIVE_INFINITY : minValue; }
	
	public double getMax()
	{ return maxValue == null ? Double.POSITIVE_INFINITY : maxValue; }
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.DOUBLE; }
	
	public final ConfigEntry simpleCopy()
	{ return new ConfigEntryDouble(getID(), defValue); }
	
	public void setDouble(double v)
	{
		value = v;
		if(minValue != null && value < minValue) value = minValue;
		if(maxValue != null && value > maxValue) value = maxValue;
	}
	
	public final int getColor()
	{ return 0xAA5AE8; }
	
	public final String getDefValueString()
	{ return Double.toString(defValue); }
	
	public final void fromJson(JsonElement json)
	{ setDouble(json.getAsDouble()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(getAsDouble()); }
	
	public final NBTBase serializeNBT()
	{ return new NBTTagDouble(getAsDouble()); }
	
	public final void deserializeNBT(NBTBase nbt)
	{ setDouble(((NBTBase.NBTPrimitive) nbt).getDouble()); }
	
	public final void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setDouble("D", defValue);
		if(minValue != null) nbt.setDouble("Min", minValue);
		if(maxValue != null) nbt.setDouble("Max", maxValue);
	}
	
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		defValue = nbt.getDouble("D");
		minValue = nbt.hasKey("Min") ? nbt.getDouble("Min") : null;
		maxValue = nbt.hasKey("Max") ? nbt.getDouble("Max") : null;
	}
	
	public String getMinValueString()
	{
		if(minValue != null)
		{
			return LMStringUtils.formatDouble(minValue);
		}
		
		return null;
	}
	
	public String getMaxValueString()
	{
		if(maxValue != null)
		{
			return LMStringUtils.formatDouble(maxValue);
		}
		
		return null;
	}
	
	public final String getAsString()
	{ return Double.toString(getAsDouble()); }
	
	public final int getAsInt()
	{ return (int) getAsDouble(); }
	
	public double getAsDouble()
	{ return value; }
	
	public final boolean getAsBoolean()
	{ return getAsDouble() != 0D; }
}