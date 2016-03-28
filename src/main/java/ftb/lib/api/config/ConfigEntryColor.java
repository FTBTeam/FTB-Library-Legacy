package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.LMColor;
import net.minecraft.nbt.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigEntryColor extends ConfigEntry
{
	public final LMColor.RGB value;
	public final LMColor.RGB defValue;
	
	public ConfigEntryColor(String id, LMColor def)
	{
		super(id);
		value = new LMColor.RGB();
		defValue = new LMColor.RGB();
		
		if(def != null)
		{
			value.set(def);
			defValue.set(def);
		}
	}
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.COLOR; }
	
	public final ConfigEntry simpleCopy()
	{ return new ConfigEntryColor(getID(), defValue); }
	
	public final int getColor()
	{ return value.color(); }
	
	public final String getDefValueString()
	{ return defValue.toString(); }
	
	public final void fromJson(JsonElement json)
	{ value.setRGBA(json.getAsInt()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(value.color()); }
	
	public final NBTBase serializeNBT()
	{ return new NBTTagInt(value.color()); }
	
	public final void deserializeNBT(NBTBase nbt)
	{ value.setRGBA(((NBTBase.NBTPrimitive) nbt).getInt()); }
	
	public final void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("D", defValue.color());
	}
	
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		defValue.setRGBA(nbt.getInteger("D"));
	}
	
	public final String getAsString()
	{ return value.toString(); }
	
	public int getAsInt()
	{ return value.color(); }
}