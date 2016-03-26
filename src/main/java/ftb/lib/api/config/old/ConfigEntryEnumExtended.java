package ftb.lib.api.config.old;

import com.google.gson.*;
import latmod.lib.*;

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
	
	public PrimitiveType getType()
	{ return PrimitiveType.ENUM; }
	
	public int getIndex()
	{ return values.indexOf(value); }
	
	public final void setJson(JsonElement o)
	{ value = o.getAsString(); }
	
	public final JsonElement getJson()
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
	
	public String getDefValue()
	{ return defValue; }
}