package ftb.lib.api.config;

import com.google.gson.*;
import ftb.lib.api.IClickable;
import net.minecraft.nbt.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigEntryBool extends ConfigEntry implements IClickable
{
	private boolean value;
	public boolean defValue;
	
	private static final byte BYTE_0 = 0;
	private static final byte BYTE_1 = 1;
	
	public ConfigEntryBool(String id, boolean def)
	{
		super(id);
		defValue = def;
		value = def;
	}
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.BOOLEAN; }
	
	public final ConfigEntry simpleCopy()
	{ return new ConfigEntryBool(getID(), defValue); }
	
	public void setBoolean(boolean v)
	{ value = v; }
	
	public final int getColor()
	{ return getAsBoolean() ? 0x33AA33 : 0xD52834; }
	
	public final String getDefValueString()
	{ return defValue ? "true" : "false"; }
	
	public final void fromJson(JsonElement json)
	{ setBoolean(json.getAsBoolean()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(getAsBoolean()); }
	
	public final NBTBase serializeNBT()
	{ return new NBTTagByte(getAsBoolean() ? BYTE_1 : BYTE_0); }
	
	public final void deserializeNBT(NBTBase nbt)
	{ setBoolean(((NBTBase.NBTPrimitive) nbt).getByte() != 0); }
	
	public final void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		nbt.setBoolean("D", defValue);
	}
	
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		defValue = nbt.getBoolean("D");
	}
	
	public void onClicked()
	{ setBoolean(!getAsBoolean()); }
	
	public final String getAsString()
	{ return getAsBoolean() ? "true" : "false"; }
	
	public final int getAsInt()
	{ return getAsBoolean() ? 1 : 0; }
	
	public final double getAsDouble()
	{ return getAsBoolean() ? 1D : 0D; }
	
	public boolean getAsBoolean()
	{ return value; }
}