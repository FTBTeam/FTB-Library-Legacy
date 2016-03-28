package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.annotations.INumberBoundsContainer;
import net.minecraft.nbt.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigEntryInt extends ConfigEntry implements INumberBoundsContainer
{
	private int value;
	public int defValue;
	private Integer minValue;
	private Integer maxValue;
	
	public ConfigEntryInt(String id, int def)
	{
		super(id);
		defValue = def;
		value = def;
	}
	
	public final void setBounds(double min, double max)
	{
		minValue = (min == Double.NEGATIVE_INFINITY) ? null : (int) min;
		maxValue = (max == Double.POSITIVE_INFINITY) ? null : (int) max;
	}
	
	public final double getMin()
	{ return minValue == null ? Double.NEGATIVE_INFINITY : minValue.doubleValue(); }
	
	public final double getMax()
	{ return maxValue == null ? Double.POSITIVE_INFINITY : maxValue.doubleValue();}
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.INT; }
	
	public final ConfigEntry simpleCopy()
	{ return new ConfigEntryInt(getID(), defValue); }
	
	public void set(int v)
	{
		value = v;
		if(minValue != null && value < minValue) value = minValue;
		if(maxValue != null && value > maxValue) value = maxValue;
	}
	
	public final int getColor()
	{ return 0xAA5AE8; }
	
	public final String getDefValueString()
	{ return Integer.toString(defValue); }
	
	public final void fromJson(JsonElement json)
	{ set(json.getAsInt()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(getAsInt()); }
	
	public final NBTBase serializeNBT()
	{ return new NBTTagInt(getAsInt()); }
	
	public final void deserializeNBT(NBTBase nbt)
	{ set(((NBTBase.NBTPrimitive) nbt).getInt()); }
	
	public final void writeExtendedNBT(NBTTagCompound nbt)
	{
		nbt.setInteger("D", defValue);
		if(minValue != null) nbt.setInteger("Min", minValue);
		if(maxValue != null) nbt.setInteger("Max", maxValue);
	}
	
	public final void readExtendedNBT(NBTTagCompound nbt)
	{
		defValue = nbt.getInteger("D");
		minValue = nbt.hasKey("Min") ? nbt.getInteger("Min") : null;
		maxValue = nbt.hasKey("Max") ? nbt.getInteger("Max") : null;
	}
	
	public final String getAsString()
	{ return Integer.toString(getAsInt()); }
	
	public int getAsInt()
	{ return value; }
	
	public final double getAsDouble()
	{ return getAsInt(); }
	
	public final boolean getAsBoolean()
	{ return getAsInt() != 0; }
	
	public final String getMinValueString()
	{
		if(minValue != null)
		{
			return Integer.toString(minValue.intValue());
		}
		
		return null;
	}
	
	public final String getMaxValueString()
	{
		if(maxValue != null)
		{
			return Integer.toString(maxValue.intValue());
		}
		
		return null;
	}
}