package ftb.lib.api.config;

import com.google.gson.JsonElement;
import latmod.lib.*;
import latmod.lib.annotations.*;
import latmod.lib.util.FinalIDObject;
import net.minecraft.util.IJsonSerializable;

public abstract class ConfigEntry extends FinalIDObject implements IInfoContainer, IFlagContainer, IJsonSerializable
{
	public ConfigGroup parentGroup;
	private String[] info;
	protected byte flags = 0;
	
	ConfigEntry(String id)
	{
		super(id);
	}
	
	public abstract ConfigType getConfigType();
	public abstract void func_152753_a(JsonElement o);
	public abstract JsonElement getSerializableElement();
	public abstract void write(ByteIOStream io);
	public abstract void read(ByteIOStream io);
	
	public int getColor()
	{ return 0x999999; }
	
	public void writeExtended(ByteIOStream io)
	{ write(io); }
	
	public void readExtended(ByteIOStream io)
	{ read(io); }
	
	public void onPreLoaded() { }
	
	public void onPostLoaded() { }
	
	public String getFullID()
	{
		if(parentGroup == null) return getID();
		return parentGroup.getFullID() + '.' + getID();
	}
	
	public String getDefValueString()
	{ return null; }
	
	public String getMinValueString()
	{ return null; }
	
	public String getMaxValueString()
	{ return null; }
	
	public ConfigEntry copy()
	{
		ConfigEntry e = getConfigType().createNew(getID());
		e.func_152753_a(getSerializableElement());
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
	
	public void setFlag(byte flag, boolean b)
	{ flags = Bits.setBit(flags, flag, b); }
	
	public boolean getFlag(byte flag)
	{ return Bits.getBit(flags, flag); }
	
	public void setInfo(String[] s)
	{ info = s; }
	
	public String[] getInfo()
	{ return new String[0]; }
}