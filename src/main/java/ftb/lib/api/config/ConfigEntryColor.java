package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;

public class ConfigEntryColor extends ConfigEntry
{
	public final LMColor.RGB value;
	public final LMColor.RGB defValue;
	
	public ConfigEntryColor(String id, LMColor def)
	{
		super(id);
		value = new LMColor.RGB();
		value.set(def);
		
		defValue = new LMColor.RGB();
		defValue.set(def);
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.COLOR; }
	
	public int getColor()
	{ return value.color(); }
	
	public final void func_152753_a(JsonElement o)
	{ value.setRGBA(o.getAsInt()); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(value.color()); }
	
	public void write(ByteIOStream io)
	{ io.writeInt(value.color()); }
	
	public void read(ByteIOStream io)
	{ value.setRGBA(io.readInt()); }
	
	public void writeExtended(ByteIOStream io)
	{
		write(io);
		io.writeInt(defValue.color());
	}
	
	public void readExtended(ByteIOStream io)
	{
		read(io);
		defValue.setRGBA(io.readInt());
	}
	
	public String getAsString()
	{ return value.toString(); }
	
	public int getAsInt()
	{ return value.color(); }
	
	public String getDefValueString()
	{ return defValue.toString(); }
}