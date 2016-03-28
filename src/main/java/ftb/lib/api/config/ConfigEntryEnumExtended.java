package ftb.lib.api.config;

import com.google.gson.*;
import ftb.lib.api.IClickable;
import net.minecraft.nbt.*;

import java.util.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public final class ConfigEntryEnumExtended extends ConfigEntry implements IClickable
{
	private int value;
	private int defValue;
	private final List<String> values;
	
	public ConfigEntryEnumExtended(String id)
	{
		super(id);
		values = new ArrayList<>();
	}
	
	public ConfigEntryType getType()
	{ return ConfigEntryType.ENUM; }
	
	public ConfigEntry simpleCopy()
	{ return new ConfigEntryEnumExtended(getID()); }
	
	public int getColor()
	{ return 0x0094FF; }
	
	public String getDefValueString()
	{ return values.get(defValue); }
	
	public void fromJson(JsonElement json)
	{ value = values.indexOf(json.getAsString()); }
	
	public JsonElement getSerializableElement()
	{ return new JsonPrimitive(values.get(value)); }
	
	public NBTBase serializeNBT()
	{ return new NBTTagString(values.get(value)); }
	
	public void deserializeNBT(NBTBase nbt)
	{ value = values.indexOf(((NBTTagString) nbt).getString()); }
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setInteger("D", defValue);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		defValue = nbt.getInteger("D");
	}
	
	public void onClicked()
	{ value = (value + 1) % values.size(); }
	
	public String getAsString()
	{ return values.get(value); }
	
	public int getAsInt()
	{ return value; }
	
	public boolean getAsBoolean()
	{ return !values.get(value).equals("-"); }
}