package ftb.lib.api.config;

import com.google.gson.*;
import latmod.lib.*;
import latmod.lib.annotations.*;

import java.lang.reflect.Field;
import java.util.*;

public class ConfigGroup extends ConfigEntry
{
	public final Map<String, ConfigEntry> entryMap;
	
	public ConfigGroup(String s)
	{
		super(s);
		entryMap = new LinkedHashMap<>();
	}
	
	public ConfigType getConfigType()
	{ return ConfigType.GROUP; }
	
	public final List<ConfigEntry> entries()
	{ return LMMapUtils.values(entryMap, null); }
	
	public ConfigFile getConfigFile()
	{ return (parentGroup != null) ? parentGroup.getConfigFile() : null; }
	
	public ConfigGroup add(ConfigEntry e, boolean copy)
	{
		if(e != null)
		{
			if(copy)
			{
				ConfigEntry e1 = e.copy();
				entryMap.put(e1.getID(), e1);
				e1.parentGroup = this;
			}
			else
			{
				entryMap.put(e.getID(), e);
				e.parentGroup = this;
			}
		}
		
		return this;
	}
	
	public ConfigGroup addAll(Class<?> c, Object parent, boolean copy)
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
							add(entry, copy);
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
	
	public ConfigEntry copy()
	{
		ConfigGroup g = new ConfigGroup(getID());
		for(ConfigEntry e : entryMap.values())
			g.add(e, true);
		return g;
	}
	
	public final void func_152753_a(JsonElement o0)
	{
		if(o0 == null || !o0.isJsonObject()) return;
		
		entryMap.clear();
		
		JsonObject o = o0.getAsJsonObject();
		
		for(Map.Entry<String, JsonElement> e : o.entrySet())
		{
			ConfigEntry entry = new ConfigEntryCustom(e.getKey());
			
			if(!e.getValue().isJsonNull())
			{
				entry.func_152753_a(e.getValue());
				entry.onPostLoaded();
			}
			
			add(entry, false);
		}
	}
	
	public final JsonElement getSerializableElement()
	{
		JsonObject o = new JsonObject();
		
		for(ConfigEntry e : entries())
		{
			if(!e.getFlag(Flags.EXCLUDED))
			{
				e.onPreLoaded();
				o.add(e.getID(), e.getSerializableElement());
			}
		}
		
		return o;
	}
	
	public String getAsString()
	{ return getSerializableElement().toString(); }
	
	public String[] getAsStringArray()
	{ return LMListUtils.toStringArray(entries()); }
	
	public boolean getAsBoolean()
	{ return !entryMap.isEmpty(); }
	
	public int getAsInt()
	{ return entryMap.size(); }
	
	public void write(ByteIOStream io)
	{
		io.writeShort(entryMap.size());
		for(ConfigEntry e : entryMap.values())
		{
			e.onPreLoaded();
			io.writeByte(e.getConfigType().ordinal());
			io.writeUTF(e.getID());
			e.write(io);
		}
	}
	
	public void read(ByteIOStream io)
	{
		int s = io.readUnsignedShort();
		entryMap.clear();
		for(int i = 0; i < s; i++)
		{
			int type = io.readUnsignedByte();
			String id = io.readUTF();
			ConfigEntry e = ConfigType.VALUES[type].createNew(id);
			e.read(io);
			add(e, false);
		}
	}
	
	public void writeExtended(ByteIOStream io)
	{
		io.writeShort(entryMap.size());
		for(ConfigEntry e : entryMap.values())
		{
			e.onPreLoaded();
			io.writeByte(e.getConfigType().ordinal());
			io.writeUTF(e.getID());
			e.writeExtended(io);
			
			String[] info = getInfo();
			
			e.setFlag(Flags.HAS_INFO, info != null && info.length > 0);
			io.writeByte(e.flags);
			
			if(e.getFlag(Flags.HAS_INFO))
			{
				io.writeByte(info.length);
				
				for(String s : info)
				{
					io.writeUTF(s);
				}
			}
		}
	}
	
	public void readExtended(ByteIOStream io)
	{
		int s = io.readUnsignedShort();
		entryMap.clear();
		for(int i = 0; i < s; i++)
		{
			int type = io.readUnsignedByte();
			String id = io.readUTF();
			ConfigEntry e = ConfigType.VALUES[type].createNew(id);
			e.readExtended(io);
			e.flags = io.readByte();
			
			if(e.getFlag(Flags.HAS_INFO))
			{
				String[] info = new String[io.readUnsignedByte()];
				for(int j = 0; j < info.length; j++)
				{
					info[j] = io.readUTF();
				}
				
				e.setInfo(info);
			}
			
			add(e, false);
		}
	}
	
	public int loadFromGroup(ConfigGroup l)
	{
		if(l == null || l.entryMap.isEmpty()) return 0;
		
		int result = 0;
		
		for(ConfigEntry e1 : l.entryMap.values())
		{
			ConfigEntry e0 = entryMap.get(e1.getID());
			
			if(e0 != null)
			{
				if(e0.getAsGroup() != null)
				{
					ConfigGroup g1 = new ConfigGroup(e1.getID());
					g1.func_152753_a(e1.getSerializableElement());
					result += e0.getAsGroup().loadFromGroup(g1);
				}
				else
				{
					try
					{
						//System.out.println("Value " + e1.getFullID() + " set from " + e0.getSerializableElement() + " to " + e1.getSerializableElement());
						e0.func_152753_a(e1.getSerializableElement());
						e0.onPostLoaded();
						result++;
					}
					catch(Exception ex)
					{
						System.err.println("Can't set value " + e1.getSerializableElement() + " for '" + e0.parentGroup.getID() + "." + e0.getID() + "' (type:" + e0.getConfigType() + ")");
						System.err.println(ex.toString());
					}
				}
			}
		}
		
		if(result > 0) onLoadedFromGroup(l);
		return result;
	}
	
	protected void onLoadedFromGroup(ConfigGroup l)
	{
	}
	
	public boolean hasKey(Object key)
	{ return entryMap.containsKey(LMUtils.getID(key)); }
	
	public ConfigEntry getEntry(Object key)
	{ return entryMap.get(LMUtils.getID(key)); }
	
	public ConfigGroup getGroup(Object key)
	{
		ConfigEntry e = getEntry(key);
		return (e == null) ? null : e.getAsGroup();
	}
	
	public List<ConfigGroup> getGroups()
	{
		ArrayList<ConfigGroup> list = new ArrayList<>();
		for(ConfigEntry e : entryMap.values())
		{
			ConfigGroup g = e.getAsGroup();
			if(g != null) list.add(g);
		}
		return list;
	}
	
	public ConfigGroup getAsGroup()
	{ return this; }
	
	public int getTotalEntryCount()
	{
		int count = 0;
		
		for(ConfigEntry e : entryMap.values())
		{
			if(e.getAsGroup() == null) count++;
			else count += e.getAsGroup().getTotalEntryCount();
		}
		
		return count;
	}
	
	public int getDepth()
	{ return (parentGroup == null) ? 0 : (parentGroup.getDepth() + 1); }
	
	public ConfigGroup generateSynced(boolean copy)
	{
		ConfigGroup out = new ConfigGroup(getID());
		
		for(ConfigEntry e : entryMap.values())
		{
			if(e.getFlag(Flags.SYNC))
			{
				out.add(e, copy);
			}
			else if(e.getAsGroup() != null)
			{
				ConfigGroup g = e.getAsGroup().generateSynced(copy);
				if(!g.entryMap.isEmpty()) out.add(g, false);
			}
		}
		
		return out;
	}
}