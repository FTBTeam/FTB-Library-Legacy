package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.FTBLibPlugin;
import com.feed_the_beast.ftbl.api.IFTBLibPlugin;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.INotification;
import com.feed_the_beast.ftbl.api.IPackModes;
import com.feed_the_beast.ftbl.api.IRankConfig;
import com.feed_the_beast.ftbl.api.ISharedClientData;
import com.feed_the_beast.ftbl.api.ISharedServerData;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.events.LoadWorldDataEvent;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.gui.IContainerProvider;
import com.feed_the_beast.ftbl.client.EnumNotificationDisplay;
import com.feed_the_beast.ftbl.lib.AsmHelper;
import com.feed_the_beast.ftbl.lib.BroadcastSender;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.internal.FTBLibNotifications;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import com.feed_the_beast.ftbl.lib.util.LMUtils;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import com.feed_the_beast.ftbl.net.MessageDisplayGuide;
import com.feed_the_beast.ftbl.net.MessageEditConfig;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayer;
import com.feed_the_beast.ftbl.net.MessageNotifyPlayerCustom;
import com.feed_the_beast.ftbl.net.MessageOpenGui;
import com.feed_the_beast.ftbl.net.MessageReload;
import com.google.common.base.Preconditions;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class FTBLibAPI_Impl implements FTBLibAPI
{
	public static final boolean LOG_NET = System.getProperty("ftbl.logNetwork", "0").equals("1");
	private Collection<IFTBLibPlugin> plugins;

	public void init(ASMDataTable table)
	{
		plugins = AsmHelper.findPlugins(table, IFTBLibPlugin.class, FTBLibPlugin.class);

		for (IFTBLibPlugin p : plugins)
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
	public void loadWorldData(MinecraftServer server)
	{
		MinecraftForge.EVENT_BUS.post(new LoadWorldDataEvent(server));
	}

	@Override
	public void reload(Side side, ICommandSender sender, EnumReloadType type)
	{
		long ms = System.currentTimeMillis();
		boolean serverSide = side.isServer();

		if (serverSide)
		{
			Preconditions.checkNotNull(Universe.INSTANCE, "Can't reload yet!");
			FTBLibMod.PROXY.reloadConfig(LoaderState.ModState.AVAILABLE);
		}

		MinecraftForge.EVENT_BUS.post(new ReloadEvent(side, sender, type));

		if (serverSide && ServerUtils.hasOnlinePlayers())
		{
			for (EntityPlayerMP ep : ServerUtils.getServer().getPlayerList().getPlayers())
			{
				NBTTagCompound syncData = new NBTTagCompound();
				IForgePlayer p = Universe.INSTANCE.getPlayer(ep);
				FTBLibModCommon.SYNCED_DATA.forEach((key, value) -> syncData.setTag(key, value.writeSyncData(ep, p)));
				new MessageReload(type, syncData).sendTo(ep);
			}
		}

		if (type != EnumReloadType.CREATED)
		{
			(serverSide ? FTBLibLang.RELOAD_SERVER : FTBLibLang.RELOAD_CLIENT).printChat(BroadcastSender.INSTANCE, (System.currentTimeMillis() - ms) + "ms");

			if (serverSide && type == EnumReloadType.RELOAD_COMMAND)
			{
				sendNotification(null, FTBLibNotifications.RELOAD_CLIENT_CONFIG);
			}
		}

		FTBLibFinals.LOGGER.info("Reloaded " + side + " on packmode '" + getSidedData(side).getPackMode() + "'");
	}

	@Override
	public void openGui(ResourceLocation guiID, EntityPlayerMP player, BlockPos pos, @Nullable NBTTagCompound data)
	{
		IContainerProvider containerProvider = FTBLibModCommon.GUI_CONTAINER_PROVIDERS.get(guiID);

		if (containerProvider == null)
		{
			return;
		}

		Container c = containerProvider.getContainer(player, pos, data);

		player.getNextWindowId();
		player.closeContainer();

		if (c != null)
		{
			player.openContainer = c;
		}

		player.openContainer.windowId = player.currentWindowId;
		player.openContainer.addListener(player);
		new MessageOpenGui(guiID, pos, data, player.currentWindowId).sendTo(player);
	}

	@Override
	public void sendNotification(@Nullable EntityPlayer player, INotification n)
	{
		if (player != null && player.world.isRemote)
		{
			FTBLibMod.PROXY.displayNotification(EnumNotificationDisplay.SCREEN, n);
		}
		else if (SharedServerData.INSTANCE.notifications.containsKey(n.getId()))
		{
			new MessageNotifyPlayer(n.getId()).sendTo(player);
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
	public void displayGuide(EntityPlayer player, GuidePage page)
	{
		if (player.world.isRemote)
		{
			FTBLibMod.PROXY.displayGuide(page);
		}
		else
		{
			new MessageDisplayGuide(page).sendTo(player);
		}
	}

	@Override
	public IConfigValue getConfigValueFromID(String id)
	{
		IConfigValueProvider provider = FTBLibModCommon.CONFIG_VALUE_PROVIDERS.get(id);
		Preconditions.checkNotNull(provider, "Unknown Config ID: " + id);
		return provider.createConfigValue();
	}

	@Override
	public Map<String, IRankConfig> getRankConfigRegistry()
	{
		return FTBLibModCommon.RANK_CONFIGS_MIRROR;
	}

	@Override
	public void handleMessage(MessageBase<?> message, MessageContext context, Side side)
	{
		if (side.isServer())
		{
			context.getServerHandler().player.mcServer.addScheduledTask(() ->
			{
				message.onMessage(LMUtils.cast(message), context.getServerHandler().player);

				if (LOG_NET)
				{
					LMUtils.DEV_LOGGER.info("TX MessageBase: " + message.getClass().getName());
				}
			});
		}
		else
		{
			FTBLibMod.PROXY.handleClientMessage(message);
		}
	}
}