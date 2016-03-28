package ftb.lib.api.config;

import com.google.gson.JsonElement;
import net.minecraft.nbt.NBTBase;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public final class ConfigEntryNBTBase extends ConfigEntry
{
	public NBTBase base;
	
	public ConfigEntryNBTBase(String id)
	{
		super(id);
		base = null;
	}
	
	public ConfigEntryType getType()
	{ return ConfigEntryType.NBT_BASE; }
	
	public ConfigEntry simpleCopy()
	{ return new ConfigEntryNBTBase(getID()); }
	
	public int getColor()
	{ return 0x999999; }
	
	public String getAsString()
	{ return ""; }
	
	public String getDefValueString()
	{ return ""; }
	
	public void fromJson(JsonElement json)
	{
		throw new UnsupportedOperationException();
	}
	
	public JsonElement getSerializableElement()
	{
		throw new UnsupportedOperationException();
	}
	
	public NBTBase serializeNBT()
	{ return base; }
	
	public void deserializeNBT(NBTBase nbt)
	{ base = nbt; }
}