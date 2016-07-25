package com.feed_the_beast.ftbl.api.config;

import com.feed_the_beast.ftbl.util.FTBLib;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;

@SideOnly(Side.CLIENT)
public final class ClientConfigRegistry
{
    private static final ConfigFile file = new ConfigFile();

    //new ResourceLocation(FTBLibFinals.MOD_ID, "client_config")
    public static final ConfigContainer CONTAINER = new ConfigContainer()
    {
        @Override
        public ConfigGroup createGroup()
        {
            return file;
        }

        @Override
        public ITextComponent getConfigTitle()
        {
            return new TextComponentString("client_settings"); //TODO: Lang
        }

        @Override
        public void saveConfig(ICommandSender sender, NBTTagCompound nbt, JsonObject json)
        {
            file.loadFromGroup(json);
            ClientConfigRegistry.saveConfig();
        }
    };

    public static void saveConfig()
    {
        if(file.getFile() == null)
        {
            file.setFile(new File(FTBLib.folderLocal, "client/config.json"));
            file.load();
        }

        file.save();
    }

    /**
     * Do this before postInit()
     */
    public static void addGroup(String id, Class<?> c)
    {
        file.addGroup(id, c);
    }

    public static void addGroup(String id, ConfigGroup group)
    {
        file.add(id, group);
    }
}