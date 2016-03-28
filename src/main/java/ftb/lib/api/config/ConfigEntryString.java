package ftb.lib.api.config;

import com.google.gson.*;
import net.minecraft.nbt.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigEntryString extends ConfigEntry
{
	private String value;
	public String defValue;
	
	public ConfigEntryString(String id, String def)
	{
		super(id);
		defValue = def;
		value = def;
	}
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.STRING; }
	
	public final ConfigEntry simpleCopy()
	{ return new ConfigEntryString(getID(), defValue); }
	
	public void setString(String v)
	{ value = v; }
	
	public final int getColor()
	{ return 0xFFAA49; }
	
	public final String getDefValueString()
	{ return defValue; }
	
	public final void fromJson(JsonElement json)
	{ setString(json.getAsString()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(getAsString()); }
	
	public final NBTBase serializeNBT()
	{ return new NBTTagString(getAsString()); }
	
	public final void deserializeNBT(NBTBase nbt)
	{ setString(((NBTTagString) nbt).getString()); }
	
	public final void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setString("D", defValue);
	}
	
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		defValue = nbt.getString("D");
	}
	
	public String getAsString()
	{ return value; }
	
	public final int getAsInt()
	{ return getAsString().length(); }
	
	public final boolean getAsBoolean()
	{ return !getAsString().isEmpty(); }
}