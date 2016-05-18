package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@SideOnly(Side.CLIENT)
public final class ClientConfigRegistry
{
    private static final ConfigFile file = new ConfigFile("client_config");
    
    public static IConfigProvider provider()
    {
        return new IConfigProvider()
        {
            @Override
            public String getGroupTitle(ConfigGroup g)
            { return I18n.format(g.getID()); }
            
            @Override
            public String getEntryTitle(ConfigEntry e)
            { return I18n.format(e.getFullID()); }
            
            @Override
            public ConfigFile getConfigGroup()
            {
                if(file.getFile() == null)
                {
                    file.setFile(new File(FTBLib.folderLocal, "client/config.json"));
                    file.load();
                }
                
                return file;
            }
            
            @Override
            public void save()
            { getConfigGroup().save(); }
        };
    }
    
    /**
     * Do this before postInit()
     */
    public static void addGroup(String id, Class<?> c)
    { file.addGroup(id, c); }
    
    public static void add(ConfigGroup group)
    { file.add(group, false); }
}