package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import latmod.lib.IntList;
import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;
import java.util.List;

public class ConfigEntryIntList extends ConfigEntry
{
	public final IntList defValue;
	private IntList value;
	
	public ConfigEntryIntList(String id, IntList def)
	{
		super(id);
		defValue = def == null ? new IntList() : def.copy();
		value = defValue.copy();
	}
	
	@Override
	public ConfigEntryType getConfigType()
	{ return ConfigEntryType.INT_ARRAY; }
	
	@Override
	public int getColor()
	{ return 0xAA5AE8; }
	
	public ConfigEntryIntList(String id, int[] def)
	{ this(id, IntList.asList(def)); }
	
	public void set(IntList l)
	{ value = (l == null || l.isEmpty()) ? new IntList() : l.copy(); }
	
	@Override
	public void fromJson(JsonElement o)
	{
		JsonArray a = o.getAsJsonArray();
		IntList l = new IntList(a.size());
		for(int i = 0; i < l.size(); i++)
			l.set(i, a.get(i).getAsInt());
		set(l);
	}
	
	@Override
	public JsonElement getSerializableElement()
	{
		JsonArray a = new JsonArray();
		value = getAsIntList();
		for(int i = 0; i < value.size(); i++)
			a.add(new JsonPrimitive(value.get(i)));
		return a;
	}
	
	@Override
	public String getAsString()
	{ return getAsIntList().toString(); }
	
	@Override
	public List<String> getAsStringList()
	{
		List<String> l = new ArrayList<>();
		for(Integer i : getAsIntList())
			l.add(i.toString());
		return l;
	}
	
	@Override
	public IntList getAsIntList()
	{ return value; }
	
	@Override
	public String getDefValueString()
	{ return defValue.toString(); }
	
	@Override
	public void writeToNBT(NBTTagCompound tag, boolean extended)
	{
		super.writeToNBT(tag, extended);
		
		int[] ai = getAsIntList().toArray();
		
		if(ai.length > 0)
		{
			tag.setIntArray("V", ai);
		}
		
		if(extended && !defValue.isEmpty())
		{
			tag.setIntArray("D", defValue.toArray());
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound tag, boolean extended)
	{
		super.readFromNBT(tag, extended);
		set(tag.hasKey("V") ? new IntList(tag.getIntArray("V")) : null);
		
		if(extended)
		{
			defValue.clear();
			
			if(tag.hasKey("D"))
			{
				defValue.addAll(tag.getIntArray("D"));
			}
		}
	}
}