package ftb.lib.api.config;

import com.google.gson.*;
import ftb.lib.api.IClickable;
import net.minecraft.nbt.NBTBase;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigEntryButton extends ConfigEntry implements IClickable
{
	public ConfigEntryButton(String id)
	{
		super(id);
	}
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.BUTTON; }
	
	public final ConfigEntry simpleCopy()
	{ return new ConfigEntryButton(getID()); }
	
	public final int getColor()
	{ return 0xFFAA00; }
	
	public final String getAsString()
	{ return ". . ."; }
	
	public final String getDefValueString()
	{ return ". . ."; }
	
	public final void fromJson(JsonElement json)
	{
	}
	
	public final JsonElement getSerializableElement()
	{ return JsonNull.INSTANCE; }
	
	public final NBTBase serializeNBT()
	{ return null; }
	
	public final void deserializeNBT(NBTBase nbt)
	{
	}
	
	public void onClicked()
	{
	}
}