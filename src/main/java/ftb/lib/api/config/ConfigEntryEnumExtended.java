package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.ByteIOStream;

import java.util.*;

public class ConfigEntryEnumExtended extends ConfigEntry implements IClickableConfigEntry
{
	public final List<String> values;
	public String value;
	public String defValue;
	
	public ConfigEntryEnumExtended(String id)
	{
		super(id);
		values = new ArrayList<>();
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.ENUM; }
	
	public int getColor()
	{ return 0x0094FF; }
	
	public int getIndex()
	{ return values.indexOf(value); }
	
	public final void func_152753_a(JsonElement o)
	{ value = o.getAsString(); }
	
	public final JsonElement getSerializableElement()
	{ return new JsonPrimitive(value); }
	
	public void write(ByteIOStream io)
	{ io.writeUTF(value); }
	
	public void read(ByteIOStream io)
	{ value = io.readUTF(); }
	
	public void readExtended(ByteIOStream io)
	{
		values.clear();
		int s = io.readUnsignedByte();
		for(int i = 0; i < s; i++)
			values.add(io.readUTF());
		value = values.get(io.readUnsignedByte());
		defValue = values.get(io.readUnsignedByte());
	}
	
	public void onClicked()
	{ value = values.get((getIndex() + 1) % values.size()); }
	
	public String getAsString()
	{ return value; }
	
	public boolean getAsBoolean()
	{ return value != null; }
	
	public int getAsInt()
	{ return getIndex(); }
	
	public String getDefValueString()
	{ return defValue; }
}