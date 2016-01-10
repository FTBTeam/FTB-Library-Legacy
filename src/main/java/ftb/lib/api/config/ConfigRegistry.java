package ftb.lib.api.config;

import com.google.gson.reflect.TypeToken;
import ftb.lib.FTBLib;
import latmod.lib.LMJsonUtils;
import latmod.lib.config.*;

import java.io.File;
import java.util.*;

public class ConfigRegistry
{
	public static final HashMap<String, Provider> map = new HashMap<>();
	public static final HashMap<String, Provider> tempMap = new HashMap<>();
	public static final ConfigGroup synced = new ConfigGroup("synced");
	
	public static void add(Provider p)
	{ if(p != null) map.put(p.getID(), p); }
	
	public static void add(ConfigFile e)
	{
		if(e != null)
		{
			add(new ConfigFileProvider(e));
			ConfigGroup g = e.configGroup.generateSynced(false);
			if(!g.entries().isEmpty()) synced.add(g, false);
		}
	}
	
	public static void reload()
	{
		for(Provider p : map.values())
		{
			if(p instanceof ConfigFileProvider) ((ConfigFileProvider) p).file.load();
		}
		
		FTBLib.dev_logger.info("Loading override configs");
		Map<String, ConfigGroup> overrides = LMJsonUtils.fromJsonFile(new File(FTBLib.folderModpack, "overrides.json"), new TypeToken<Map<String, ConfigGroup>>() { }.getType());
		
		if(overrides != null && !overrides.isEmpty())
		{
			for(String key : overrides.keySet())
			{
				ConfigGroup ol = overrides.get(key);
				
				int result;
				Provider p = map.get(key);
				ConfigGroup o = (p == null) ? null : p.getGroup();
				if(o != null && (result = o.loadFromGroup(ol)) > 0)
				{
					FTBLib.dev_logger.info("Config overriden: " + result);
					o.parentFile.save();
				}
				else FTBLib.dev_logger.info("Didnt load anything from " + ol);
			}
		}
	}
	
	public static interface Provider
	{
		String getID();
		
		ConfigGroup getGroup();
	}
	
	public static class ConfigFileProvider implements Provider
	{
		public final ConfigFile file;
		
		public ConfigFileProvider(ConfigFile f)
		{ file = f; }
		
		public String getID()
		{ return file.ID; }
		
		public ConfigGroup getGroup()
		{ return file.configGroup; }
	}
}