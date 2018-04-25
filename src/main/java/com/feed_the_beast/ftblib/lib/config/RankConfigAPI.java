package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.context.IContext;
import net.minecraftforge.server.permission.context.PlayerContext;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class RankConfigAPI
{
	private static IRankConfigHandler handler = null;

	public static void setHandler(IRankConfigHandler h)
	{
		Preconditions.checkNotNull(h, "Permission handler can't be null!");

		if (handler != null)
		{
			FTBLib.LOGGER.warn("Replacing " + handler.getClass().getName() + " with " + h.getClass().getName());
		}

		handler = h;
	}

	public static ConfigValue get(MinecraftServer server, GameProfile profile, Node node, @Nullable IContext context)
	{
		Preconditions.checkNotNull(profile, "GameProfile can't be null!");
		Preconditions.checkNotNull(node, "Config node can't be null!");
		return handler == null ? ConfigNull.INSTANCE : handler.getConfigValue(server, profile, node, context);
	}

	public static ConfigValue get(EntityPlayerMP player, Node node)
	{
		Preconditions.checkNotNull(player, "Player can't be null!");
		return get(player.mcServer, player.getGameProfile(), node, new PlayerContext(player));
	}

	public static ConfigValue get(ForgePlayer player, Node node)
	{
		Preconditions.checkNotNull(player, "Player can't be null!");
		return player.isOnline() ? get(player.getPlayer(), node) : get(player.team.universe.server, player.getProfile(), node, null);
	}
}