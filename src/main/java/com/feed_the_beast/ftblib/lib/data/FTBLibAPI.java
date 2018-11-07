package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibCommon;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.FTBLibNotifications;
import com.feed_the_beast.ftblib.events.IReloadHandler;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigNull;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.ConfigValueProvider;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.math.Ticks;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.text_components.Notification;
import com.feed_the_beast.ftblib.net.MessageCloseGui;
import com.feed_the_beast.ftblib.net.MessageEditConfig;
import com.feed_the_beast.ftblib.net.MessageSyncData;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class FTBLibAPI
{
	public static void reloadServer(Universe universe, ICommandSender sender, EnumReloadType type, ResourceLocation id)
	{
		long ms = System.currentTimeMillis();
		universe.clearCache();

		HashSet<ResourceLocation> failed = new HashSet<>();
		ServerReloadEvent event = new ServerReloadEvent(universe, sender, type, id, failed);

		for (Map.Entry<ResourceLocation, IReloadHandler> entry : FTBLibCommon.RELOAD_IDS.entrySet())
		{
			try
			{
				if (event.reload(entry.getKey()) && !entry.getValue().onReload(event))
				{
					event.failedToReload(entry.getKey());
				}
			}
			catch (Exception ex)
			{
				event.failedToReload(entry.getKey());

				if (FTBLibConfig.debugging.print_more_errors)
				{
					ex.printStackTrace();
				}
			}
		}

		event.post();

		for (EntityPlayerMP player : universe.server.getPlayerList().getPlayers())
		{
			ForgePlayer p = universe.getPlayer(player);
			new MessageSyncData(false, player, p).sendTo(player);
		}

		String millis = (System.currentTimeMillis() - ms) + "ms";

		if (type == EnumReloadType.RELOAD_COMMAND)
		{
			for (EntityPlayerMP player : universe.server.getPlayerList().getPlayers())
			{
				Notification notification = Notification.of(FTBLibNotifications.RELOAD_SERVER);
				notification.addLine(FTBLib.lang(player, "ftblib.lang.reload_server", millis));

				if (event.isClientReloadRequired())
				{
					notification.addLine(FTBLib.lang(player, "ftblib.lang.reload_client", StringUtils.color(new TextComponentString("F3 + T"), TextFormatting.GOLD)));
				}

				if (!failed.isEmpty())
				{
					notification.addLine(StringUtils.color(FTBLib.lang(player, "ftblib.lang.reload_failed"), TextFormatting.RED));
					FTBLib.LOGGER.warn("These IDs failed to reload:");

					for (ResourceLocation f : failed)
					{
						notification.addLine(StringUtils.color(new TextComponentString(f.toString()), TextFormatting.RED));
						FTBLib.LOGGER.warn("- " + f);
					}
				}

				notification.setImportant(true);
				notification.setTimer(Ticks.SECOND.x(7));
				notification.send(universe.server, player);
			}
		}

		universe.server.reload();
		FTBLib.LOGGER.info("Reloaded server in " + millis);
	}

	public static void editServerConfig(EntityPlayerMP player, ConfigGroup group, IConfigCallback callback)
	{
		FTBLibCommon.TEMP_SERVER_CONFIG.put(player.getGameProfile().getId(), new FTBLibCommon.EditingConfig(group, callback));
		new MessageEditConfig(group).sendTo(player);
	}

	public static ConfigValue createConfigValueFromId(String id)
	{
		if (id.isEmpty())
		{
			return ConfigNull.INSTANCE;
		}

		ConfigValueProvider provider = FTBLibCommon.CONFIG_VALUE_PROVIDERS.get(id);
		Objects.requireNonNull(provider, "Unknown Config ID: " + id);
		return provider.get();
	}

	public static void sendCloseGuiPacket(EntityPlayerMP player)
	{
		new MessageCloseGui().sendTo(player);
	}

	/**
	 * Helper method for other mods so they don't have to deal with other classes than this
	 */
	public static boolean arePlayersInSameTeam(UUID player1, UUID player2)
	{
		if (!Universe.loaded())
		{
			return false;
		}
		else if (player1 == player2 || player1.equals(player2))
		{
			return true;
		}

		ForgePlayer p1 = Universe.get().getPlayer(player1);

		if (p1 == null || !p1.hasTeam())
		{
			return false;
		}

		ForgePlayer p2 = Universe.get().getPlayer(player2);
		return p2 != null && p2.hasTeam() && p1.team.equalsTeam(p2.team);
	}

	public static boolean isPlayerInTeam(UUID player, String team)
	{
		if (!Universe.loaded())
		{
			return false;
		}

		ForgePlayer p = Universe.get().getPlayer(player);

		if (p == null)
		{
			return false;
		}

		return p.hasTeam() ? p.team.getID().equals(team) : team.isEmpty();
	}

	public static boolean isPlayerInTeam(UUID player, int team)
	{
		if (!Universe.loaded())
		{
			return false;
		}

		ForgePlayer p = Universe.get().getPlayer(player);

		if (p == null)
		{
			return false;
		}

		return p.hasTeam() ? p.team.getUID() == team : team == 0;
	}

	public static String getTeam(UUID player)
	{
		if (!Universe.loaded())
		{
			return "";
		}

		ForgePlayer p = Universe.get().getPlayer(player);
		return p == null ? "" : p.team.getID();
	}

	public static short getTeamID(UUID player)
	{
		if (!Universe.loaded())
		{
			return 0;
		}

		ForgePlayer p = Universe.get().getPlayer(player);
		return p == null ? 0 : p.team.getUID();
	}
}