package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.FTBLibPlugin;
import com.feed_the_beast.ftbl.api.IFTBLibPlugin;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.IPackModes;
import com.feed_the_beast.ftbl.api.ISharedClientData;
import com.feed_the_beast.ftbl.api.ISharedServerData;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.gui.IContainerProvider;
import com.feed_the_beast.ftbl.lib.AsmHelper;
import com.feed_the_beast.ftbl.lib.BroadcastSender;
import com.feed_the_beast.ftbl.lib.info.InfoPage;
import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.internal.FTBLibNotifications;
import com.feed_the_beast.ftbl.lib.util.LMServerUtils;
import com.feed_the_beast.ftbl.net.MessageDisplayInfo;
import com.feed_the_beast.ftbl.net.MessageEditConfig;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayer;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayerCustom;
import com.feed_the_beast.ftbl.net.MessageOpenGui;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.google.common.base.Preconditions;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public class FTBLibAPI_Impl implements FTBLibAPI
{
    private Collection<IFTBLibPlugin> plugins;

    public void init(ASMDataTable table)
    {
        plugins = AsmHelper.findPlugins(table, IFTBLibPlugin.class, FTBLibPlugin.class);

        for(IFTBLibPlugin p : plugins)
        {
            p.init(this);
        }
    }

    @Override
    public Collection<IFTBLibPlugin> getAllPlugins()
    {
        return plugins;
    }

    @Override
    public Collection<ITickable> ticking()
    {
        return TickHandler.INSTANCE.TICKABLES;
    }

    @Override
    public IPackModes getPackModes()
    {
        return PackModes.INSTANCE;
    }

    @Override
    public ISharedServerData getServerData()
    {
        return SharedServerData.INSTANCE;
    }

    @Override
    public ISharedClientData getClientData()
    {
        return SharedClientData.INSTANCE;
    }

    @Override
    @Nullable
    public IUniverse getUniverse()
    {
        return Universe.INSTANCE;
    }

    @Override
    public void addServerCallback(int timer, Runnable runnable)
    {
        TickHandler.INSTANCE.addServerCallback(timer, runnable);
    }

    @Override
    public void reload(ICommandSender sender, EnumReloadType type)
    {
        Preconditions.checkNotNull(Universe.INSTANCE, "Can't reload yet!");
        Preconditions.checkArgument(type != EnumReloadType.LOGIN, "ReloadType can't be LOGIN!");

        long ms = System.currentTimeMillis();

        if(type.reload(Side.SERVER))
        {
            FTBLibMod.PROXY.reloadConfig();

            for(IFTBLibPlugin plugin : FTBLibIntegrationInternal.API.getAllPlugins())
            {
                plugin.onReload(Side.SERVER, sender, type);
            }
        }

        if(LMServerUtils.hasOnlinePlayers())
        {
            for(EntityPlayerMP ep : LMServerUtils.getServer().getPlayerList().getPlayerList())
            {
                NBTTagCompound syncData = new NBTTagCompound();
                IForgePlayer p = Universe.INSTANCE.getPlayer(ep);
                FTBLibMod.PROXY.SYNCED_DATA.forEach((key, value) -> syncData.setTag(key, value.writeSyncData(ep, p)));
                new MessageReload(type, syncData).sendTo(ep);
            }
        }

        if(type.reload(Side.SERVER))
        {
            FTBLibLang.RELOAD_SERVER.printChat(BroadcastSender.INSTANCE, (System.currentTimeMillis() - ms) + "ms");
        }

        if(type == EnumReloadType.SERVER_ONLY_NOTIFY_CLIENT && sender instanceof EntityPlayerMP)
        {
            sendNotification((EntityPlayerMP) sender, FTBLibNotifications.RELOAD_CLIENT_CONFIG);
        }
    }

    @Override
    public void openGui(ResourceLocation guiID, EntityPlayerMP player, BlockPos pos, @Nullable NBTTagCompound data)
    {
        IContainerProvider containerProvider = FTBLibMod.PROXY.GUI_CONTAINER_PROVIDERS.get(guiID);

        if(containerProvider == null)
        {
            return;
        }

        Container c = containerProvider.getContainer(player, pos, data);

        player.getNextWindowId();
        player.closeContainer();

        if(c != null)
        {
            player.openContainer = c;
        }

        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addListener(player);
        new MessageOpenGui(guiID, pos, data, player.currentWindowId).sendTo(player);
    }

    @Override
    public void sendNotification(@Nullable EntityPlayerMP player, INotification n)
    {
        if(SharedServerData.INSTANCE.notifications.containsKey(n.getID()))
        {
            new MessageNotifyPlayer(n.getID()).sendTo(player);
        }
        else
        {
            new MessageNotifyPlayerCustom(n).sendTo(player);
        }
    }

    @Override
    public void editServerConfig(EntityPlayerMP player, @Nullable NBTTagCompound nbt, IConfigContainer configContainer)
    {
        new MessageEditConfig(player.getGameProfile().getId(), nbt, configContainer).sendTo(player);
    }

    @Override
    public void displayInfoGui(EntityPlayerMP player, InfoPage page)
    {
        new MessageDisplayInfo(page).sendTo(player);
    }

    @Override
    public IConfigValue getConfigValueFromID(String id)
    {
        IConfigValueProvider provider = FTBLibMod.PROXY.CONFIG_VALUE_PROVIDERS.get(id);
        Preconditions.checkNotNull(provider, "Unknown Config ID: " + id);
        return provider.createConfigValue();
    }
}
