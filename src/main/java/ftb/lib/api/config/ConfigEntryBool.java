package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.ByteIOStream;

public class ConfigEntryBool extends ConfigEntry implements IClickableConfigEntry
{
	public boolean defValue;
	private boolean value;
	
	public ConfigEntryBool(String id, boolean def)
	{
		super(id);
		set(def);
		defValue = def;
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.BOOLEAN; }
	
	public int getColor()
	{ return get() ? 0x33AA33 : 0xD52834; }
	
	public void set(boolean v)
	{ value = v; }
	
	public boolean get()
	{ return value; }
	
	public final void func_152753_a(JsonElement o)
	{ set(o.getAsBoolean()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(get()); }
	
	public void write(ByteIOStream io)
	{ io.writeBoolean(get()); }
	
	public void read(ByteIOStream io)
	{ set(io.readBoolean()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeBoolean(defValue);
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue = io.readBoolean();
	}
	
	public void onClicked()
	{ set(!get()); }
	
	public String getAsString()
	{ return get() ? "true" : "false"; }
	
	public boolean getAsBoolean()
	{ return get(); }
	
	public int getAsInt()
	{ return get() ? 1 : 0; }
	
	public double getAsDouble()
	{ return get() ? 1D : 0D; }
	
	public String getDefValueString()
	{ return defValue ? "true" : "false"; }
}