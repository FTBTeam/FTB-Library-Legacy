package ftb.lib.api.config;

import com.google.gson.reflect.TypeToken;
import ftb.lib.FTBLib;
import latmod.lib.*;
import latmod.lib.config.*;

import java.io.File;
import java.util.Map;

public class ConfigRegistry
{
	public static final FastMap<String, Provider> map = new FastMap<>();
	public static final ConfigGroup synced = new ConfigGroup("synced");
	private static ConfigGroup temp = null;
	
	public static void add(Provider p)
	{ if(p != null) map.put(p.getID(), p); }
	
	public static void add(ConfigFile e)
	{
		if(e != null)
		{
			add(new ConfigFileProvider(e));
			ConfigGroup g = e.configGroup.generateSynced(false);
			if(!g.entries().isEmpty())
				synced.add(g, false);
		}
	}

	public static void setTemp(ConfigGroup g)
	{ temp = g; }
	
	public static ConfigGroup getTemp(boolean remove)
	{ ConfigGroup g = temp; if(remove) temp = null; return g; }
	
	public static void reload()
	{
		for(Provider p : map.values())
		{
			if(p instanceof ConfigFileProvider)
				((ConfigFileProvider)p).file.load();
		}
		
		FTBLib.dev_logger.info("Loading override configs");
		Map<String, ConfigGroup> overrides = LMJsonUtils.fromJsonFile(new File(FTBLib.folderModpack, "overrides.json"), new TypeToken<Map<String, ConfigGroup>>(){}.getType());
		
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
		public String getID();
		public ConfigGroup getGroup();
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