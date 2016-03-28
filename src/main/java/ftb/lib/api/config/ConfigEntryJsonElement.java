package ftb.lib.api.config;

import com.google.gson.*;
import net.minecraft.nbt.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public final class ConfigEntryJsonElement extends ConfigEntry
{
	public JsonElement element;
	
	public ConfigEntryJsonElement(String id)
	{
		super(id);
		element = JsonNull.INSTANCE;
	}
	
	public ConfigEntryType getType()
	{ return ConfigEntryType.JSON_ELEMENT; }
	
	public ConfigEntry simpleCopy()
	{ return new ConfigEntryJsonElement(getID()); }
	
	public int getColor()
	{ return 0x999999; }
	
	public String getAsString()
	{ return ""; }
	
	public String getDefValueString()
	{ return ""; }
	
	public void fromJson(JsonElement json)
	{ element = json; }
	
	public JsonElement getSerializableElement()
	{ return element; }
	
	public NBTBase serializeNBT()
	{
		throw new UnsupportedOperationException();
	}
	
	public void deserializeNBT(NBTBase nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	public void writeExtendedNBT(NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
	
	public void readExtendedNBT(NBTTagCompound nbt)
	{
		throw new UnsupportedOperationException();
	}
}