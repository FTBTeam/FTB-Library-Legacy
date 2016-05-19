package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.net.MessageEditConfig;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.LMAccessToken;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.google.gson.JsonElement;
import latmod.lib.LMJsonUtils;
import latmod.lib.LMUtils;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigRegistry
{
    public static final Map<String, ConfigFile> map = new HashMap<>();
    private static final Map<String, ConfigFile> tempServerConfig = new HashMap<>();

    public static void add(ConfigFile f)
    {
        if(f != null)
        {
            map.put(f.getID(), f);
        }
    }

    public static void reload()
    {
        for(ConfigFile f : map.values())
        { f.load(); }

        FTBLib.dev_logger.info("Loading override configs");
        JsonElement overridesE = LMJsonUtils.fromJson(new File(FTBLib.folderModpack, "overrides.json"));

        if(overridesE.isJsonObject())
        {
            for(Map.Entry<String, JsonElement> e : overridesE.getAsJsonObject().entrySet())
            {
                ConfigGroup ol = new ConfigGroup(e.getKey());
                ol.fromJson(e.getValue());

                int result;
                ConfigFile f = map.get(ol.getID());
                if(f != null && (result = f.loadFromGroup(ol, false)) > 0)
                {
                    FTBLib.dev_logger.info("Config '" + e.getKey() + "' overriden: " + result);
                    f.save();
                }
                else
                {
                    FTBLib.dev_logger.info("Didnt load anything from " + e.getKey());
                }
            }
        }

        map.values().forEach(ConfigFile::save);
    }

    public static ConfigFile createTempConfig(EntityPlayerMP ep)
    {
        if(ep != null)
        {
            ConfigFile group = new ConfigFile(LMUtils.fromUUID(ep.getGameProfile().getId()));
            tempServerConfig.put(group.getID(), group);
            return group;
        }

        return null;
    }

    public static void editTempConfig(EntityPlayerMP ep, ConfigFile file, ReloadType reload)
    {
        if(ep != null && file != null && tempServerConfig.containsValue(file))
        {
            new MessageEditConfig(LMAccessToken.generate(ep), reload, file).sendTo(ep);
        }
    }

    public static ConfigFile getTempConfig(String id)
    {
        ConfigFile group = tempServerConfig.get(id);
        if(group != null)
        {
            tempServerConfig.remove(id);
        }
        return group;
    }

    public static void clearTemp()
    {
        tempServerConfig.clear();
    }
}