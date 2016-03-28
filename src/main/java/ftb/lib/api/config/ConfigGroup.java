package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.annotations.*;
import net.minecraft.nbt.*;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by LatvianModder on 26.03.2016.
 */
public class ConfigGroup extends ConfigEntry
{
	public final Map<String, ConfigEntry> entryMap;
	
	public ConfigGroup(String id)
	{
		super(id);
		entryMap = new LinkedHashMap<>();
	}
	
	public final ConfigEntryType getType()
	{ return ConfigEntryType.GROUP; }
	
	public ConfigEntry simpleCopy()
	{ return new ConfigGroup(getID()); }
	
	public final int getColor()
	{ return 0x999999; }
	
	public final String getAsString()
	{ return ""; }
	
	public final String getDefValueString()
	{ return ""; }
	
	public final void fromJson(JsonElement json)
	{
		entryMap.clear();
		
		for(Map.Entry<String, JsonElement> e : json.getAsJsonObject().entrySet())
		{
			ConfigEntryJsonElement c = new ConfigEntryJsonElement(e.getKey());
			entryMap.put(c.getID(), c);
		}
	}
	
	public final JsonElement getSerializableElement()
	{
		JsonObject o = new JsonObject();
		
		for(Map.Entry<String, ConfigEntry> e : entryMap.entrySet())
		{
			o.add(e.getKey(), e.getValue().getSerializableElement());
		}
		
		return o;
	}
	
	public final NBTBase serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		
		if(entryMap.isEmpty()) return tag;
		
		for(Map.Entry<String, ConfigEntry> e : entryMap.entrySet())
		{
			tag.setTag(e.getKey(), e.getValue().serializeNBT());
		}
		
		return tag;
	}
	
	public final void deserializeNBT(NBTBase nbt)
	{
		entryMap.clear();
		
		NBTTagCompound tag = (NBTTagCompound) nbt;
		
		if(tag != null && !tag.hasNoTags())
		{
			for(String s : tag.getKeySet())
			{
				
			}
		}
	}
	
	public final void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
	}
	
	public final void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
	}
	
	public ConfigGroup generateSynced()
	{
		ConfigGroup out = new ConfigGroup(getID());
		
		for(ConfigEntry e : entryMap.values())
		{
			if(e.getFlag(Flag.SYNC.ID))
			{
				out.add(e);
			}
			else if(e.getAsGroup() != null)
			{
				ConfigGroup g = e.getAsGroup().generateSynced();
				
				if(!g.entryMap.isEmpty())
				{
					out.add(g);
				}
			}
		}
		
		return out;
	}
	
	public ConfigGroup add(ConfigEntry e)
	{
		entryMap.put(e.getID(), e);
		e.parentGroup = this;
		return this;
	}
	
	public final ConfigGroup getAsGroup()
	{ return this; }
	
	public int loadFromGroup(ConfigGroup ol, boolean isNBT)
	{
		//FIXME: Loading from group
		return 0;
	}
	
	public ConfigGroup addAll(Class<?> c, Object parent)
	{
		try
		{
			Field f[] = c.getDeclaredFields();
			
			if(f != null && f.length > 0) for(Field aF : f)
			{
				try
				{
					aF.setAccessible(true);
					if(ConfigEntry.class.isAssignableFrom(aF.getType()))
					{
						ConfigEntry entry = (ConfigEntry) aF.get(parent);
						if(entry != null && entry != this && !(entry instanceof ConfigFile))
						{
							AnnotationHelper.inject(aF, parent, entry);
							add(entry);
						}
					}
				}
				catch(Exception e1) { }
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return this;
	}
	
	public ConfigGroup getGroup(String s)
	{
		ConfigEntry e = entryMap.get(s);
		return (e == null) ? null : e.getAsGroup();
	}
}
