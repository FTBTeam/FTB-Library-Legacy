package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.FTBLibNotifications;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IConfigManager;
import com.feed_the_beast.ftbl.api.IFTBLibRegistries;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.api_impl.config.ConfigManager;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayer;
import com.feed_the_beast.ftbl.net.MessageOpenGui;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.google.gson.JsonElement;
import com.latmod.lib.BroadcastSender;
import com.latmod.lib.util.LMFileUtils;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMNBTUtils;
import com.latmod.lib.util.LMUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.io.File;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public final class FTBLibAPI_Impl extends FTBLibAPI
{
    private static FTBLibAPI_Impl INST;
    private static Universe universe;
    private static PackModes packModes;
    private static SharedData sharedDataServer, sharedDataClient;
    private boolean hasServer = false;

    public static FTBLibAPI_Impl get()
    {
        if(INST == null)
        {
            INST = new FTBLibAPI_Impl();
        }

        return INST;
    }

    public void setHasServer(boolean b)
    {
        hasServer = b;
    }

    @Override
    public boolean hasServer()
    {
        return hasServer;
    }

    @Override
    public IConfigManager configManager()
    {
        return ConfigManager.INSTANCE;
    }

    @Override
    public IFTBLibRegistries getRegistries()
    {
        return FTBLibRegistries.INSTANCE;
    }

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
    @Nullable
    public Universe getUniverse()
    {
        return universe;
    }

    @Override
    public void addServerCallback(int timer, Runnable runnable)
    {
        FTBLibRegistries.INSTANCE.addServerCallback(timer, runnable);
    }

    @Override
    public void reload(ICommandSender sender, ReloadType type)
    {
        if(universe == null)
        {
            throw new NullPointerException("Can't reload yet!");
        }

        if(type == ReloadType.LOGIN)
        {
            throw new IllegalArgumentException("ReloadType can't be LOGIN!");
        }

        long ms = System.currentTimeMillis();

        if(type.reload(Side.SERVER))
        {
            ConfigManager.INSTANCE.reloadConfig();
            reloadPackModes();
            MinecraftForge.EVENT_BUS.post(new ReloadEvent(Side.SERVER, sender, type));
        }

        new MessageReload(type).sendTo(null);

        if(type.reload(Side.SERVER))
        {
            FTBLibLang.RELOAD_SERVER.printChat(BroadcastSender.INSTANCE, (System.currentTimeMillis() - ms) + "ms");
        }

        if(type == ReloadType.SERVER_ONLY_NOTIFY_CLIENT && sender instanceof EntityPlayerMP)
        {
            sendNotification((EntityPlayerMP) sender, FTBLibNotifications.RELOAD_CLIENT_CONFIG);
        }
    }

    @Override
    public void openGui(ResourceLocation guiID, EntityPlayerMP ep, @Nullable NBTTagCompound data)
    {
        IGuiHandler handler = FTBLibAPI.get().getRegistries().guis().get(guiID);

        if(handler == null)
        {
            return;
        }

        Container c = handler.getContainer(ep, data);

        ep.getNextWindowId();
        ep.closeContainer();

        if(c != null)
        {
            ep.openContainer = c;
        }

        ep.openContainer.windowId = ep.currentWindowId;
        ep.openContainer.addListener(ep);
        new MessageOpenGui(guiID, data, ep.currentWindowId).sendTo(ep);
    }

    @Override
    public void sendNotification(@Nullable EntityPlayerMP ep, INotification n)
    {
        new MessageNotifyPlayer(n).sendTo(ep);
    }

    // Other Methods //

    public void reloadPackModes()
    {
        File file = LMFileUtils.newFile(new File(LMUtils.folderModpack, "packmodes.json"));
        packModes = new PackModes(LMJsonUtils.fromJson(file));
        LMJsonUtils.toJson(file, packModes.toJsonObject());
    }

    public void createAndLoadWorld()
    {
        try
        {
            universe = new Universe();

            JsonElement worldData = LMJsonUtils.fromJson(new File(LMUtils.folderWorld, "world_data.json"));

            if(worldData.isJsonObject())
            {
                getSharedData(Side.SERVER).fromJson(worldData.getAsJsonObject());
            }

            universe.playerMap.clear();

            NBTTagCompound nbt = LMNBTUtils.readTag(new File(LMUtils.folderWorld, "data/FTBLib.dat"));

            if(nbt != null)
            {
                universe.deserializeNBT(nbt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void closeWorld()
    {
        universe.onClosed();
        universe = null;
    }
}
