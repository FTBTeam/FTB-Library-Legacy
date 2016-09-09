package com.feed_the_beast.ftbl.api.config;

import com.google.gson.JsonObject;
import com.latmod.lib.util.LMUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.io.File;

public final class ClientConfigRegistry
{
    private static final ConfigFile FILE = new ConfigFile();

    //new ResourceLocation(FTBLibFinals.MOD_ID, "client_config")
    public static final IConfigContainer CONTAINER = new IConfigContainer()
    {
        @Override
        public ConfigGroup createGroup()
        {
            return FILE;
        }

        @Override
        public ITextComponent getConfigTitle()
        {
            return new TextComponentString("client_settings"); //TODO: Lang
        }

        @Override
        public void saveConfig(ICommandSender sender, @Nullable NBTTagCompound nbt, JsonObject json)
        {
            FILE.loadFromGroup(json);
            ClientConfigRegistry.saveConfig();
        }
    };

    public static void saveConfig()
    {
        if(FILE.getFile() == null)
        {
            FILE.setFile(new File(LMUtils.folderLocal, "client/config.json"));
            FILE.load();
        }

        FILE.save();
    }

    /**
     * Do this before postInit()
     */
    public static void addGroup(String id, Class<?> c)
    {
        FILE.addGroup(id, c);
    }

    public static void addGroup(String id, ConfigGroup group)
    {
        FILE.add(id, group);
    }
}