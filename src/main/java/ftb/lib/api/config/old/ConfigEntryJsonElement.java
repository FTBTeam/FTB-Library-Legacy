package ftb.lib.api.config.old;

import com.google.gson.*;
import latmod.lib.*;

class ConfigEntryJsonElement extends ConfigEntry
{
	private JsonElement value;
	
	ConfigEntryJsonElement(String id)
	{
		super(id);
	}
	
	public PrimitiveType getType()
	{ return PrimitiveType.NULL; }
	
	public final void setJson(JsonElement o)
	{ value = o; }
	
	public final JsonElement getJson()
	{ return value; }
	
	public void write(ByteIOStream io)
	{ }
	
	public void read(ByteIOStream io)
	{ }
	
	public boolean isNull()
	{ return value == null || value.isJsonNull(); }
	
	public ConfigGroup getAsGroup()
	{
		if(isNull()) return null;
		if(value.isJsonObject())
			return (ConfigGroup) LMJsonUtils.deserializationContext.deserialize(value, ConfigGroup.class);
		return null;
	}
	
	public String getAsString()
	{ return isNull() ? null : String.valueOf(value); }
	
	public String[] getAsStringArray()
	{
		if(isNull()) return null;
		JsonArray a = value.getAsJsonArray();
		String[] ai = new String[a.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = (a.get(i).getAsString());
		return ai;
	}
	
	public boolean getAsBoolean()
	{ return !isNull() && value.getAsBoolean(); }
	
	public int getAsInt()
	{ return isNull() ? 0 : value.getAsInt(); }
	
	public double getAsDouble()
	{ return isNull() ? 0D : value.getAsDouble(); }
	
	public int[] getAsIntArray()
	{
		if(isNull()) return null;
		JsonArray a = value.getAsJsonArray();
		int[] ai = new int[a.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = (a.get(i).getAsInt());
		return ai;
	}
	
	public double[] getAsDoubleArray()
	{
		if(isNull()) return null;
		JsonArray a = value.getAsJsonArray();
		double[] ai = new double[a.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = (a.get(i).getAsDouble());
		return ai;
	}
	
	public String getDefValue()
	{ return null; }
}