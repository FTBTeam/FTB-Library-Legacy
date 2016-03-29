package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;

public class ConfigEntryBlank extends ConfigEntry implements IClickableConfigEntry
{
	public ConfigEntryBlank(String id)
	{ super(id); }
	
	public PrimitiveType getType()
	{ return PrimitiveType.NULL; }
	
	public final void setJson(JsonElement o)
	{ }
	
	public final JsonElement getJson()
	{ return JsonNull.INSTANCE; }
	
	public String getAsString()
	{ return ". . ."; }
	
	public void write(ByteIOStream io)
	{ }
	
	public void read(ByteIOStream io)
	{ }
	
	public void onClicked()
	{
	}
	
	public String getDefValueString()
	{ return null; }
}