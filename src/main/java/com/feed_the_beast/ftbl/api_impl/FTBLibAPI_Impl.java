package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibLang;
import com.feed_the_beast.ftbl.FTBLibNotifications;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.FTBLibAddon;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.ISyncData;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.ReloadType;
import com.feed_the_beast.ftbl.api.gui.IGuiHandler;
import com.feed_the_beast.ftbl.net.MessageEditConfig;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayer;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayerCustom;
import com.feed_the_beast.ftbl.net.MessageOpenGui;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.google.gson.JsonElement;
import com.latmod.lib.BroadcastSender;
import com.latmod.lib.util.ASMUtils;
import com.latmod.lib.util.LMFileUtils;
import com.latmod.lib.util.LMJsonUtils;
import com.latmod.lib.util.LMNBTUtils;
import com.latmod.lib.util.LMServerUtils;
import com.latmod.lib.util.LMUtils;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public enum FTBLibAPI_Impl implements FTBLibAPI, ITickable
{
    INSTANCE;

    private static class ServerTickCallback
    {
        private final int maxTick;
        private Runnable runnable;
        private int ticks = 0;

        private ServerTickCallback(int i, Runnable r)
        {
            maxTick = i;
            runnable = r;
        }

        private boolean incAndCheck()
        {
            ticks++;
            if(ticks >= maxTick)
            {
                runnable.run();
                return true;
            }

            return false;
        }
    }

    private static Universe universe;
    private static PackModes packModes;
    private static SharedData sharedDataServer, sharedDataClient;
    private boolean hasServer = false;

    public void init(ASMDataTable table)
    {
        ASMUtils.findAnnotatedObjects(table, FTBLibAPI.class, FTBLibAddon.class, (obj, field, data) -> field.set(null, INSTANCE));
        ASMUtils.findAnnotatedMethods(table, FTBLibAddon.class, (method, params, data) -> method.invoke(null));

        ConfigManager.INSTANCE.init(table);
        FTBLibRegistries.INSTANCE.init(table);
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
    public Collection<ITickable> ticking()
    {
        return TICKABLES;
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
        if(timer <= 0)
        {
            runnable.run();
        }
        else
        {
            PENDING_CALLBACKS.add(new ServerTickCallback(timer, runnable));
        }
    }

    //TODO: Make this Thread-safe
    private final List<ServerTickCallback> CALLBACKS = new ArrayList<>();
    private final List<ServerTickCallback> PENDING_CALLBACKS = new ArrayList<>();
    public final Collection<ITickable> TICKABLES = new ArrayList<>();

    @Override
    public void update()
    {
        if(!TICKABLES.isEmpty())
        {
            TICKABLES.forEach(ITickable::update);
        }

        if(!PENDING_CALLBACKS.isEmpty())
        {
            CALLBACKS.addAll(PENDING_CALLBACKS);
            PENDING_CALLBACKS.clear();
        }

        if(!CALLBACKS.isEmpty())
        {
            for(int i = CALLBACKS.size() - 1; i >= 0; i--)
            {
                if(CALLBACKS.get(i).incAndCheck())
                {
                    CALLBACKS.remove(i);
                }
            }
        }
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

        if(LMServerUtils.hasOnlinePlayers())
        {
            NBTTagCompound sharedDataTag = FTBLibAPI_Impl.INSTANCE.getSharedData(Side.SERVER).serializeNBT();

            for(EntityPlayerMP ep : LMServerUtils.getServer().getPlayerList().getPlayerList())
            {
                Map<String, NBTTagCompound> syncData = new HashMap<>();

                for(Map.Entry<ResourceLocation, ISyncData> entry : FTBLibRegistries.INSTANCE.SYNCED_DATA.getEntrySet())
                {
                    syncData.put(entry.getKey().toString(), entry.getValue().writeSyncData(ep, FTBLibAPI_Impl.INSTANCE.getUniverse().getPlayer(ep)));
                }

                new MessageReload(type, sharedDataTag, syncData).sendTo(ep);
            }
        }

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
        IGuiHandler handler = FTBLibRegistries.INSTANCE.GUIS.get(guiID);

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
        short id = FTBLibRegistries.INSTANCE.NOTIFICATIONS.getIDs().getIDFromKey(n.getID() + "@" + n.getVariant());

        if(id != 0)
        {
            new MessageNotifyPlayer(id).sendTo(ep);
        }
        else
        {
            new MessageNotifyPlayerCustom(n).sendTo(ep);
        }
    }

    @Override
    public void editServerConfig(EntityPlayerMP ep, @Nullable NBTTagCompound nbt, IConfigContainer configContainer)
    {
        new MessageEditConfig(ep.getGameProfile().getId(), nbt, configContainer).sendTo(ep);
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

        FTBLibRegistries.INSTANCE.NOTIFICATIONS.getIDs().generateIDs(FTBLibRegistries.INSTANCE.NOTIFICATIONS.getKeys());
        FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS.clear();
        FTBLibRegistries.INSTANCE.NOTIFICATIONS.getEntrySet().forEach(entry -> FTBLibRegistries.INSTANCE.CACHED_NOTIFICATIONS.put(FTBLibRegistries.INSTANCE.NOTIFICATIONS.getIDs().getIDFromKey(entry.getKey()), entry.getValue()));
    }

    public void closeWorld()
    {
        universe.onClosed();
        universe = null;
    }
}
