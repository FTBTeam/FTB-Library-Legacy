package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibEventHandler;
import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.config.ConfigContainer;
import com.feed_the_beast.ftbl.api.config.ConfigFile;
import com.feed_the_beast.ftbl.api.config.ConfigGroup;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.feed_the_beast.ftbl.util.FTBLib;
import com.feed_the_beast.ftbl.util.ReloadType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.latmod.lib.BroadcastSender;
import com.latmod.lib.json.LMJsonUtils;
import com.latmod.lib.util.LMFileUtils;
import com.latmod.lib.util.LMNBTUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public final class FTBLibAPI_Impl extends FTBLibAPI
{
    public static final FTBLibAPI_Impl INSTANCE = new FTBLibAPI_Impl();

    private static ForgeWorld world;
    private static PackModes packModes;
    private static SharedData sharedDataServer, sharedDataClient;
    public final Map<UUID, ConfigContainer> tempServerConfig = new HashMap<>();
    private final Map<String, ConfigFile> configMap = new HashMap<>();
    private final ConfigGroup mainConfigGroup = new ConfigGroup();

    public final ConfigContainer CONFIG_CONTAINER = new ConfigContainer()
    {
        @Override
        public ConfigGroup createGroup()
        {
            return mainConfigGroup;
        }

        @Override
        public ITextComponent getConfigTitle()
        {
            return new TextComponentString("Server Config"); //TODO: Lang
        }

        @Override
        public void saveConfig(ICommandSender sender, NBTTagCompound nbt, JsonObject json)
        {
            mainConfigGroup.loadFromGroup(json);
            configMap.values().forEach(ConfigFile::save);
            FTBLibAPI.INSTANCE.reload(sender, ReloadType.SERVER_ONLY, false);
        }
    };

    @Override
    public PackModes getPackModes()
    {
        if(packModes == null)
        {
            reloadPackModes();
        }

        return packModes;
    }

    @Override
    public SharedData getSharedData(Side side)
    {
        if(side.isServer())
        {
            if(sharedDataServer == null)
            {
                sharedDataServer = new SharedData(Side.SERVER);
            }

            return sharedDataServer;
        }
        else
        {
            if(sharedDataClient == null)
            {
                sharedDataClient = new SharedData(Side.CLIENT);
            }

            return sharedDataClient;
        }
    }

    @Override
    public ForgeWorld getWorld()
    {
        return world;
    }

    @Override
    public void addServerCallback(int timer, Runnable runnable)
    {
        if(timer <= 0)
        {
            runnable.run();
        }
        else
        {
            FTBLibEventHandler.pendingCallbacks.add(new FTBLibEventHandler.ServerTickCallback(timer, runnable));
        }
    }

    @Override
    public void reload(ICommandSender sender, ReloadType type, boolean login)
    {
        if(world == null)
        {
            return;
        }

        long ms = System.currentTimeMillis();

        if(type.reload(Side.SERVER))
        {
            reloadConfig();
            reloadPackModes();
            MinecraftForge.EVENT_BUS.post(new ReloadEvent(Side.SERVER, sender, type, login));
        }

        if(!login)
        {
            if(FTBLib.hasOnlinePlayers())
            {
                for(EntityPlayerMP ep : FTBLib.getServer().getPlayerList().getPlayerList())
                {
                    new MessageReload(type, world.getPlayer(ep), login).sendTo(ep);
                }
            }

            if(type.reload(Side.SERVER))
            {
                FTBLibLang.reload_server.printChat(BroadcastSender.INSTANCE, (System.currentTimeMillis() - ms) + "ms");
            }
        }
    }

    @Override
    public void registerConfigFile(String id, ConfigFile file)
    {
        if(file != null)
        {
            configMap.put(id, file);
            mainConfigGroup.add(id, file);
        }
    }

    // Other Methods //

    public void reloadPackModes()
    {
        File file = LMFileUtils.newFile(new File(FTBLib.folderModpack, "packmodes.json"));
        packModes = new PackModes(LMJsonUtils.fromJson(file));
        LMJsonUtils.toJson(file, packModes.toJsonObject());
    }

    public void reloadConfig()
    {
        configMap.values().forEach(ConfigFile::load);

        FTBLib.dev_logger.info("Loading override configs");
        JsonElement overridesE = LMJsonUtils.fromJson(new File(FTBLib.folderModpack, "overrides.json"));

        if(overridesE.isJsonObject())
        {
            int result = mainConfigGroup.loadFromGroup(overridesE.getAsJsonObject());

            if(result > 0)
            {
                FTBLib.dev_logger.info("Loaded " + result + " config overrides");

                for(ConfigFile f : configMap.values())
                {
                    f.save();
                }
            }
        }

        configMap.values().forEach(ConfigFile::save);
    }

    public void createAndLoadWorld()
    {
        try
        {
            world = new ForgeWorld();

            JsonElement worldData = LMJsonUtils.fromJson(new File(FTBLib.folderWorld, "world_data.json"));

            if(worldData.isJsonObject())
            {
                sharedDataServer.fromJson(worldData.getAsJsonObject());
            }

            world.playerMap.clear();

            NBTTagCompound nbt = LMNBTUtils.readTag(new File(FTBLib.folderWorld, "data/FTBLib.dat"));

            if(nbt != null)
            {
                world.deserializeNBT(nbt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void closeWorld()
    {
        world.onClosed();
        world = null;
    }
}
