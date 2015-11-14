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
	
	public void add(ConfigList e)
	{ if(e != null && !list.contains(e)) list.add(e); }
	
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
					FTBLib.logger.info("Config overriden: " + ol.groups);
					o.parentFile.save();
				}
				else FTBLib.logger.info("Didnt load anything from " + ol);
			}
		}
	}
}