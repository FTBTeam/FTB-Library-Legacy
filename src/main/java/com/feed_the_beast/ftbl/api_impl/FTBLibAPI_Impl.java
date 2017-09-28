package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.ISharedClientData;
import com.feed_the_beast.ftbl.api.ISharedServerData;
import com.feed_the_beast.ftbl.api.ISidebarButtonGroup;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.ServerReloadEvent;
import com.feed_the_beast.ftbl.api.player.IContainerProvider;
import com.feed_the_beast.ftbl.client.FTBLibModClient;
import com.feed_the_beast.ftbl.lib.Notification;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.ConfigValueProvider;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.config.RankConfigValueInfo;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.net.MessageBase;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import com.feed_the_beast.ftbl.lib.util.StringJoiner;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.feed_the_beast.ftbl.net.MessageEditConfig;
import com.feed_the_beast.ftbl.net.MessageOpenGui;
import com.feed_the_beast.ftbl.net.MessageSyncData;
import com.google.common.base.Preconditions;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public class FTBLibAPI_Impl extends FTBLibAPI
{
	public static final boolean LOG_NET = System.getProperty("ftbl.logNetwork", "0").equals("1");

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
	public IUniverse getUniverse()
	{
		Preconditions.checkNotNull(Universe.INSTANCE);
		return Universe.INSTANCE;
	}

	@Override
	public boolean hasUniverse()
	{
		return Universe.INSTANCE != null;
	}

	@Override
	public void reloadServer(ICommandSender sender, EnumReloadType type, ResourceLocation id)
	{
		long ms = System.currentTimeMillis();

		Preconditions.checkState(hasUniverse(), "Can't reload yet!");

		HashSet<ResourceLocation> failed = new HashSet<>();
		ServerReloadEvent event = new ServerReloadEvent(sender, type, id, failed);
		event.post();

		if (ServerUtils.hasOnlinePlayers())
		{
			for (EntityPlayerMP player : ServerUtils.getServer().getPlayerList().getPlayers())
			{
				IForgePlayer p = Universe.INSTANCE.getPlayer(player);
				new MessageSyncData(player, p).sendTo(player);
			}
		}

		String millis = (System.currentTimeMillis() - ms) + "ms";

		if (type == EnumReloadType.RELOAD_COMMAND)
		{
			Notification notification = Notification.of(FTBLibFinals.get("reload_server"));
			notification.addLine(FTBLibLang.RELOAD_SERVER.textComponent(millis));

			if (event.isClientReloadRequired())
			{
				notification.addLine(FTBLibLang.RELOAD_CLIENT.textComponent(StringUtils.color(new TextComponentString("F3 + T"), TextFormatting.GOLD)));
			}

			if (!failed.isEmpty())
			{
				notification.addLine(StringUtils.color(FTBLibLang.RELOAD_FAILED.textComponent(), TextFormatting.RED));
				String ids = StringJoiner.with(", ").join(failed);
				notification.addLine(StringUtils.color(new TextComponentString(ids), TextFormatting.RED));
				FTBLibFinals.LOGGER.warn(FTBLibLang.RELOAD_FAILED.translate() + " " + ids);
			}

			notification.setImportant(true);
			notification.setTimer(140);
			notification.send(null);
		}

		FTBLibFinals.LOGGER.info("Reloaded server in " + millis);
	}

	@Override
	public void openGui(ResourceLocation guiId, EntityPlayerMP player, BlockPos pos, @Nullable NBTTagCompound data)
	{
		IContainerProvider containerProvider = FTBLibModCommon.GUI_CONTAINER_PROVIDERS.get(guiId);

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
		new MessageOpenGui(guiId, pos, data, player.currentWindowId).sendTo(player);
	}

	@Override
	public void editServerConfig(EntityPlayerMP player, ConfigGroup group, IConfigCallback callback)
	{
		FTBLibModCommon.TEMP_SERVER_CONFIG.put(player.getGameProfile().getId(), new FTBLibModCommon.EditingConfig(group, callback));
		new MessageEditConfig(group).sendTo(player);
	}

	@Override
	public ConfigValue getConfigValueFromID(String id)
	{
		ConfigValueProvider provider = FTBLibModCommon.CONFIG_VALUE_PROVIDERS.get(id);
		Preconditions.checkNotNull(provider, "Unknown Config ID: " + id);
		return provider.get();
	}

	@Override
	public Map<String, RankConfigValueInfo> getRankConfigRegistry()
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
				message.onMessage(CommonUtils.cast(message), context.getServerHandler().player);

				if (LOG_NET)
				{
					CommonUtils.DEV_LOGGER.info("TX MessageBase: " + message.getClass().getName());
				}
			});
		}
		else
		{
			FTBLibMod.PROXY.handleClientMessage(message);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public List<ISidebarButtonGroup> getSidebarButtonGroups()
	{
		return FTBLibModClient.SIDEBAR_BUTTON_GROUPS;
	}
}