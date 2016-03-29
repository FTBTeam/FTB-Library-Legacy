package ftb.lib.api.config;

import com.google.gson.JsonElement;
import latmod.lib.*;
import latmod.lib.annotations.*;
import latmod.lib.util.FinalIDObject;

public abstract class ConfigEntry extends FinalIDObject implements IInfoContainer, IFlagContainer
{
	public ConfigGroup parentGroup;
	private String[] info;
	protected byte flags = 0;
	
	ConfigEntry(String id)
	{
		super(id);
		if(id == null || id.isEmpty()) throw new NullPointerException("Config ID can't be null!");
	}
	
	public abstract PrimitiveType getType();
	public abstract void setJson(JsonElement o);
	public abstract JsonElement getJson();
	public abstract void write(ByteIOStream io);
	public abstract void read(ByteIOStream io);
	
	public void writeExtended(ByteIOStream io)
	{ write(io); }
	
	public void readExtended(ByteIOStream io)
	{ read(io); }
	
	public final String getPrettyJsonString(boolean pretty)
	{ return LMJsonUtils.toJson(LMJsonUtils.getGson(pretty), getJson()); }
	
	public static ConfigEntry getEntry(PrimitiveType t, String id)
	{
		if(t == null) return null;
		else if(t == PrimitiveType.NULL) return new ConfigEntryBlank(id);
		else if(t == PrimitiveType.MAP) return new ConfigGroup(id);
		else if(t == PrimitiveType.BOOLEAN) return new ConfigEntryBool(id, false);
		else if(t == PrimitiveType.DOUBLE) return new ConfigEntryDouble(id, 0D);
		else if(t == PrimitiveType.INT) return new ConfigEntryInt(id, 0);
		else if(t == PrimitiveType.INT_ARRAY) return new ConfigEntryIntArray(id, (IntList) null);
		else if(t == PrimitiveType.STRING) return new ConfigEntryString(id, null);
		else if(t == PrimitiveType.STRING_ARRAY) return new ConfigEntryStringArray(id);
		else if(t == PrimitiveType.ENUM) return new ConfigEntryEnumExtended(id);
		else if(t == PrimitiveType.COLOR) return new ConfigEntryColor(id, new LMColor.RGB());
		return null;
	}
	
	public void onPreLoaded() { }
	
	public void onPostLoaded() { }
	
	public String getFullID()
	{
		if(parentGroup == null) return getID();
		return parentGroup.getFullID() + '.' + getID();
	}
	
	public String getDefValueString()
	{ return getAsString(); }
	
	public String getMinValueString()
	{ return null; }
	
	public String getMaxValueString()
	{ return null; }
	
	public ConfigEntry copy()
	{
		ConfigEntry e = ConfigEntry.getEntry(getType(), getID());
		e.setJson(getJson());
		return e;
	}
	
	public final String toString()
	{ return getAsString(); }
	
	public abstract String getAsString();
	
	public String[] getAsStringArray()
	{ return new String[] {getAsString()}; }
	
	public boolean getAsBoolean()
	{ return false; }
	
	public int getAsInt()
	{ return 0; }
	
	public double getAsDouble()
	{ return 0D; }
	
	public int[] getAsIntArray()
	{ return new int[] {getAsInt()}; }
	
	public double[] getAsDoubleArray()
	{ return new double[] {getAsDouble()}; }
	
	public ConfigGroup getAsGroup()
	{ return null; }
	
	public void setFlag(IFlag flag, boolean b)
	{ flags = Bits.setBit(flags, flag.getFlagID(), b); }
	
	public boolean getFlag(IFlag flag)
	{ return Bits.getBit(flags, flag.getFlagID()); }
	
	public void setInfo(String[] s)
	{ info = s; }
	
	public String[] getInfo()
	{ return new String[0]; }
}