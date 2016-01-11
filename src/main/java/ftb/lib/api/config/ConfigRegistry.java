package ftb.lib.api.config;

import com.google.gson.reflect.TypeToken;
import ftb.lib.FTBLib;
import latmod.lib.LMJsonUtils;
import latmod.lib.config.*;

import java.io.File;
import java.util.*;

public class ConfigRegistry
{
	public static final HashMap<String, IConfigFile> map = new HashMap<>();
	public static final ConfigGroup synced = new ConfigGroup("synced");
	
	public static void add(IConfigFile f)
	{
		if(f != null)
		{
			ConfigGroup g = f.getGroup();
			
			if(g != null && g.ID != null)
			{
				map.put(g.ID, f);
				ConfigGroup g1 = g.generateSynced(false);
				if(!g1.entryMap.isEmpty()) synced.add(g1, false);
			}
		}
	}
	
	public static void reload()
	{
		for(IConfigFile f : map.values())
			f.load();
		
		FTBLib.dev_logger.info("Loading override configs");
		Map<String, ConfigGroup> overrides = LMJsonUtils.fromJsonFile(new File(FTBLib.folderModpack, "overrides.json"), new TypeToken<Map<String, ConfigGroup>>() { }.getType());
		
		if(overrides != null && !overrides.isEmpty())
		{
			for(Map.Entry<String, ConfigGroup> e : overrides.entrySet())
			{
				ConfigGroup ol = e.getValue();
				
				int result;
				IConfigFile f = map.get(e.getKey());
				if(f != null && (result = f.getGroup().loadFromGroup(ol)) > 0)
				{
					FTBLib.dev_logger.info("Config '" + e.getKey() + "' overriden: " + result);
					f.save();
				}
				else FTBLib.dev_logger.info("Didnt load anything from " + e.getKey());
			}
		}
	}
}