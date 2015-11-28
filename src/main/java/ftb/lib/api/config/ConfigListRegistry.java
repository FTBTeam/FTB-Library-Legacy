package ftb.lib.api.config;

import java.io.File;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import ftb.lib.FTBLib;
import latmod.lib.*;
import latmod.lib.config.*;

public class ConfigListRegistry
{
	public static final ConfigListRegistry instance = new ConfigListRegistry();
	public final FastList<ConfigList> list = new FastList<ConfigList>();
	public final FastList<ConfigList> synced = new FastList<ConfigList>();
	
	public void add(ConfigList l)
	{
		if(l != null && !list.contains(l))
		{
			list.add(l);
			
			for(ConfigGroup g : l.groups)
			{
				for(ConfigEntry e : g.entries)
				{
					if(e.shouldSync())
					{
						ConfigList l1 = ConfigListRegistry.instance.synced.getObj(e.parentGroup.parentList.toString());
						if(l1 == null)
						{
							l1 = new ConfigList();
							l1.groups = new FastList<ConfigGroup>();
							l1.setID(e.parentGroup.parentList.toString());
							ConfigListRegistry.instance.synced.add(l1);
						}
						
						ConfigGroup g1 = l1.groups.getObj(e.parentGroup.toString());
						if(g1 == null)
						{
							g1 = new ConfigGroup(e.parentGroup.toString());
							l1.add(g1);
						}
						
						g1.add(e);
					}
				}
			}
		}
	}
	
	public void add(ConfigFile e)
	{ if(e != null) add(e.configList); }
	
	public static void reloadInstance()
	{
		for(int i = 0; i < instance.list.size(); i++)
		{
			ConfigList l = instance.list.get(i);
			if(l.parentFile != null) l.parentFile.load();
		}
		
		Map<String, ConfigList> overrides = LMJsonUtils.fromJsonFile(new File(FTBLib.folderModpack, "overrides.json"), new TypeToken<Map<String, ConfigList>>(){}.getType());
		
		if(overrides != null && !overrides.isEmpty())
		{
			for(String key : overrides.keySet())
			{
				ConfigList ol = overrides.get(key);
				ol.setID(key);
				
				ConfigList o = instance.list.getObj(key);
				if(o != null && o.loadFromList(ol))
				{
					FTBLib.dev_logger.info("Config overriden: " + ol.groups);
					o.parentFile.save();
				}
				else FTBLib.dev_logger.info("Didnt load anything from " + ol);
			}
		}
	}
}