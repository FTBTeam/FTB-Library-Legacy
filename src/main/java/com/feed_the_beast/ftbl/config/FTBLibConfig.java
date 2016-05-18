package com.feed_the_beast.ftbl.config;

import com.feed_the_beast.ftbl.api.config.ConfigFile;
import com.feed_the_beast.ftbl.api.config.ConfigRegistry;
import com.feed_the_beast.ftbl.util.FTBLib;

import java.io.File;

public class FTBLibConfig
{
    public static final ConfigFile configFile = new ConfigFile("ftblib");
    
    public static void load()
    {
        configFile.setFile(new File(FTBLib.folderLocal, "ftblib.json"));
        configFile.setDisplayName("FTBLib");
        configFile.addGroup("commands", FTBLibConfigCmd.class);
        ConfigRegistry.add(configFile);
        configFile.load();
    }
}