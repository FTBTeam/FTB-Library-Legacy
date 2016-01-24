package ftb.lib.api.config;

import com.google.gson.JsonElement;
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
				if(!g1.entryMap().isEmpty()) synced.add(g1, false);
			}
		}
	}
	
	public static void reload()
	{
		for(IConfigFile f : map.values())
			f.load();
		
		FTBLib.dev_logger.info("Loading override configs");
		JsonElement overridesE = LMJsonUtils.fromJson(new File(FTBLib.folderModpack, "overrides.json"));
		
		if(overridesE.isJsonObject())
		{
			for(Map.Entry<String, JsonElement> e : overridesE.getAsJsonObject().entrySet())
			{
				ConfigGroup ol = new ConfigGroup(e.getKey());
				ol.setJson(e.getValue());
				
				int result;
				IConfigFile f = map.get(ol.ID);
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