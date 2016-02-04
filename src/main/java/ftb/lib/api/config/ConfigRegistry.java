package ftb.lib.api.config;

import com.google.gson.JsonElement;
import ftb.lib.*;
import ftb.lib.mod.net.MessageEditConfig;
import latmod.lib.LMJsonUtils;
import latmod.lib.config.*;
import latmod.lib.json.UUIDTypeAdapterLM;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.util.*;

public class ConfigRegistry
{
	public static final HashMap<String, IConfigFile> map = new HashMap<>();
	public static final ConfigGroup synced = new ConfigGroup("synced");
	private static final HashMap<String, ConfigGroup> tempServerConfig = new HashMap<>();
	
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
		
		for(IConfigFile f : map.values())
			f.save();
	}
	
	public static ConfigGroup createTempConfig(EntityPlayerMP ep)
	{
		if(ep != null)
		{
			ConfigGroup group = new ConfigGroup(UUIDTypeAdapterLM.getString(ep.getGameProfile().getId()));
			tempServerConfig.put(group.ID, group);
			return group;
		}
		
		return null;
	}
	
	public static void editTempConfig(EntityPlayerMP ep, ConfigGroup group)
	{
		if(ep != null && group != null && tempServerConfig.containsValue(group))
			new MessageEditConfig(LMAccessToken.generate(ep), group).sendTo(ep);
	}
	
	public static ConfigGroup getTempConfig(String id)
	{
		ConfigGroup group = tempServerConfig.get(id);
		if(group != null) tempServerConfig.remove(id);
		return group;
	}
	
	public static void clearTemp()
	{ tempServerConfig.clear(); }
}