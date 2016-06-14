package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.google.gson.JsonElement;
import latmod.lib.json.LMJsonUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConfigRegistry
{
    public static final Map<UUID, ConfigContainer> tempServerConfig = new HashMap<>();
    private static final Map<String, ConfigFile> map = new HashMap<>();
    private static final ConfigGroup mainGroup = new ConfigGroup();

    public static final ConfigContainer CONTAINER = new ConfigContainer(new ResourceLocation(FTBLibFinals.MOD_ID, "config"))
    {
        @Override
        public ConfigGroup createGroup()
        {
            return mainGroup;
        }

        @Override
        public ITextComponent getConfigTitle()
        {
            return new TextComponentString("Server Config"); //TODO: Lang
        }

        @Override
        public void saveConfig(EntityPlayer player, NBTTagCompound nbt, ConfigGroup config)
        {
            mainGroup.loadFromGroup(config, false);
            FTBLib.reload(player, ReloadType.SERVER_ONLY, false);
        }
    };

    public static void add(String id, ConfigFile f)
    {
        if(f != null)
        {
            map.put(id, f);
            mainGroup.add(id, f);
        }
    }

    public static void reload()
    {
        for(ConfigFile f : map.values())
        {
            f.load();
        }

        FTBLib.dev_logger.info("Loading override configs");
        JsonElement overridesE = LMJsonUtils.fromJson(new File(FTBLib.folderModpack, "overrides.json"));

        if(overridesE.isJsonObject())
        {
            for(Map.Entry<String, JsonElement> e : overridesE.getAsJsonObject().entrySet())
            {
                ConfigGroup ol = new ConfigGroup();
                ol.fromJson(e.getValue());

                int result;
                ConfigFile f = map.get(e.getKey());
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
}