package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLibFinals;
import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.FTBLibModCommon;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.events.player.IContainerProvider;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.ConfigValueProvider;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.lib.util.StringJoiner;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.text_components.Notification;
import com.feed_the_beast.ftblib.net.MessageCloseGui;
import com.feed_the_beast.ftblib.net.MessageEditConfig;
import com.feed_the_beast.ftblib.net.MessageOpenGui;
import com.feed_the_beast.ftblib.net.MessageSyncData;
import com.google.common.base.Preconditions;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author LatvianModder
 */
public class FTBLibAPI
{
	public static void reloadServer(ICommandSender sender, EnumReloadType type, ResourceLocation id)
	{
		long ms = System.currentTimeMillis();

		Preconditions.checkState(Universe.loaded(), "Can't reload yet!");

		HashSet<ResourceLocation> failed = new HashSet<>();
		ServerReloadEvent event = new ServerReloadEvent(sender, type, id, failed);
		event.post();

		if (ServerUtils.hasOnlinePlayers())
		{
			for (EntityPlayerMP player : ServerUtils.getPlayers())
			{
				ForgePlayer p = Universe.get().getPlayer(player);
				new MessageSyncData(player, p).sendTo(player);
			}
		}

		String millis = (System.currentTimeMillis() - ms) + "ms";

		if (type == EnumReloadType.RELOAD_COMMAND)
		{
			for (EntityPlayerMP player : ServerUtils.getPlayers())
			{
				Notification notification = Notification.of(FTBLibFinals.get("reload_server"));
				notification.addLine(FTBLibLang.RELOAD_SERVER.textComponent(player, millis));

				if (event.isClientReloadRequired())
				{
					notification.addLine(FTBLibLang.RELOAD_CLIENT.textComponent(player, StringUtils.color(new TextComponentString("F3 + T"), TextFormatting.GOLD)));
				}

				if (!failed.isEmpty())
				{
					notification.addLine(StringUtils.color(FTBLibLang.RELOAD_FAILED.textComponent(player), TextFormatting.RED));
					String ids = StringJoiner.with(", ").join(failed);
					notification.addLine(StringUtils.color(new TextComponentString(ids), TextFormatting.RED));
					FTBLibFinals.LOGGER.warn(FTBLibLang.RELOAD_FAILED.translate() + " " + ids);
				}

				notification.setImportant(true);
				notification.setTimer(140);
				notification.send(player);
			}
		}

		FTBLibFinals.LOGGER.info("Reloaded server in " + millis);
	}

	public static void openGui(ResourceLocation guiId, EntityPlayerMP player, BlockPos pos, @Nullable NBTTagCompound data)
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

	public static void editServerConfig(EntityPlayerMP player, ConfigGroup group, IConfigCallback callback)
	{
		FTBLibModCommon.TEMP_SERVER_CONFIG.put(player.getGameProfile().getId(), new FTBLibModCommon.EditingConfig(group, callback));
		new MessageEditConfig(group).sendTo(player);
	}

	public static ConfigValue getConfigValueFromId(String id)
	{
		ConfigValueProvider provider = FTBLibModCommon.CONFIG_VALUE_PROVIDERS.get(id);
		Objects.requireNonNull(provider, "Unknown Config ID: " + id);
		return provider.get();
	}

	public static void sendCloseGuiPacket(EntityPlayerMP player)
	{
		new MessageCloseGui().sendTo(player);
	}
}