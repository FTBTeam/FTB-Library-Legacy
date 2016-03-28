package ftb.lib.api.config;

import latmod.lib.Bits;
import latmod.lib.annotations.*;
import latmod.lib.util.FinalIDObject;
import net.minecraft.nbt.*;
import net.minecraft.util.IJsonSerializable;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public abstract class ConfigEntry extends FinalIDObject implements IJsonSerializable, INBTSerializable<NBTBase>, IInfoContainer, IFlagContainer
{
	public byte flags = 0;
	public ConfigGroup parentGroup;
	private String[] info;
	
	ConfigEntry(String id)
	{
		super(id);
	}
	
	public final void setFlag(byte flag, boolean v)
	{ flags = Bits.setBit(flags, flag, v); }
	
	public final boolean getFlag(byte flag)
	{ return Bits.getBit(flags, flag); }
	
	public String toString()
	{ return getID() + '=' + getAsString(); }
	
	public String getFullID()
	{
		if(parentGroup == null) return getID();
		return parentGroup.getFullID() + '.' + getID();
	}
	
	public ConfigEntry copy()
	{
		ConfigEntry e = simpleCopy();
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		e.readFromNBT(tag);
		return e;
	}
	
	public abstract ConfigEntryType getType();
	public abstract ConfigEntry simpleCopy();
	public abstract String getDefValueString();
	public abstract int getColor();
	
	public String getMinValueString()
	{ return null; }
	
	public String getMaxValueString()
	{ return null; }
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setByte("T", (byte) getType().ordinal());
		NBTBase n = serializeNBT();
		if(n != null) nbt.setTag("V", n);
		if(flags != 0) nbt.setByte("F", flags);
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		deserializeNBT(nbt.getTag("V"));
		flags = nbt.getByte("F");
	}
	
	public void setInfo(String[] s)
	{ info = s; }
	
	public String[] getInfo()
	{ return info; }
	
	public ConfigGroup getAsGroup()
	{ return null; }
	
	public abstract String getAsString();
	
	public int getAsInt()
	{
		throw new UnsupportedOperationException();
	}
	
	public double getAsDouble()
	{
		throw new UnsupportedOperationException();
	}
	
	public boolean getAsBoolean()
	{
		throw new UnsupportedOperationException();
	}
	
	public List<String> getAsStringList()
	{
		return Collections.singletonList(getAsString());
	}
}