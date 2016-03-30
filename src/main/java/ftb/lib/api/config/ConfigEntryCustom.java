package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.ByteIOStream;
import latmod.lib.json.JsonElementIO;

public class ConfigEntryCustom extends ConfigEntry implements IClickableConfigEntry
{
	private JsonElement value;
	
	public ConfigEntryCustom(String id)
	{
		super(id);
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.CUSTOM; }
	
	public int getColor()
	{ return 0xFFAA00; }
	
	public void setJson(JsonElement o)
	{ value = o == JsonNull.INSTANCE ? null : o; }
	
	public JsonElement getJson()
	{ return value == null ? JsonNull.INSTANCE : value; }
	
	public void write(ByteIOStream io)
	{
		JsonElementIO.write(io, getJson());
	}
	
	public void read(ByteIOStream io)
	{
		setJson(JsonElementIO.read(io));
	}
	
	public ConfigGroup getAsGroup()
	{
		JsonElement e = getJson();
		if(e.isJsonNull()) return null;
		else if(e.isJsonObject())
		{
			ConfigGroup group = new ConfigGroup(getID());
			group.setJson(e);
			return group;
		}
		return null;
	}
	
	public String getAsString()
	{
		JsonElement e = getJson();
		return e.isJsonNull() ? ". . ." : String.valueOf(e);
	}
	
	public String[] getAsStringArray()
	{
		JsonElement e = getJson();
		if(e.isJsonNull()) return new String[0];
		JsonArray a = e.getAsJsonArray();
		String[] ai = new String[a.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = (a.get(i).getAsString());
		return ai;
	}
	
	public boolean getAsBoolean()
	{
		JsonElement e = getJson();
		return e.isJsonNull() ? false : e.getAsBoolean();
	}
	
	public int getAsInt()
	{
		JsonElement e = getJson();
		return e.isJsonNull() ? 0 : e.getAsInt();
	}
	
	public double getAsDouble()
	{
		JsonElement e = getJson();
		return e.isJsonNull() ? 0D : e.getAsDouble();
	}
	
	public int[] getAsIntArray()
	{
		JsonElement e = getJson();
		if(e.isJsonNull()) return null;
		JsonArray a = e.getAsJsonArray();
		int[] ai = new int[a.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = (a.get(i).getAsInt());
		return ai;
	}
	
	public double[] getAsDoubleArray()
	{
		JsonElement e = getJson();
		if(e.isJsonNull()) return null;
		JsonArray a = e.getAsJsonArray();
		double[] ai = new double[a.size()];
		for(int i = 0; i < ai.length; i++)
			ai[i] = (a.get(i).getAsDouble());
		return ai;
	}
	
	public void onClicked()
	{
	}
}