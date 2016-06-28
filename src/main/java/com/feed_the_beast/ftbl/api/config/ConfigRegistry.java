package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.FTBLibFinals;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.json.LMJsonUtils;
import net.minecraft.command.ICommandSender;
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
        public void saveConfig(ICommandSender sender, NBTTagCompound nbt, JsonObject json)
        {
            mainGroup.loadFromGroup(json);
            map.values().forEach(ConfigFile::save);
            FTBLib.reload(sender, ReloadType.SERVER_ONLY, false);
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
        map.values().forEach(ConfigFile::load);

        FTBLib.dev_logger.info("Loading override configs");
        JsonElement overridesE = LMJsonUtils.fromJson(new File(FTBLib.folderModpack, "overrides.json"));

        if(overridesE.isJsonObject())
        {
            int result = mainGroup.loadFromGroup(overridesE.getAsJsonObject());

            if(result > 0)
            {
                FTBLib.dev_logger.info("Loaded " + result + " config overrides");

                for(ConfigFile f : map.values())
                {
                    f.save();
                }
            }
        }

        map.values().forEach(ConfigFile::save);
    }
}