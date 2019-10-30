package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.events.RegisterRankConfigEvent;
import com.feed_the_beast.ftblib.events.RegisterRankConfigHandlerEvent;
import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class RankConfigAPI
{
	private static IRankConfigHandler handler = null;

	private static void setHandler(IRankConfigHandler h)
	{
		Preconditions.checkNotNull(h, "Permission handler can't be null!");
		FTBLib.LOGGER.warn("Replacing " + handler.getClass().getName() + " with " + h.getClass().getName());
		handler = h;
	}

	public static IRankConfigHandler getHandler()
	{
		if (handler == null)
		{
			handler = DefaultRankConfigHandler.INSTANCE;
			new RegisterRankConfigHandlerEvent(RankConfigAPI::setHandler).post();
			new RegisterRankConfigEvent(handler::registerRankConfig).post();
		}

		return handler;
	}

	public static ConfigValue get(MinecraftServer server, GameProfile profile, Node node)
	{
		Preconditions.checkNotNull(profile, "GameProfile can't be null!");
		Preconditions.checkNotNull(node, "Config node can't be null!");
		return getHandler().getConfigValue(server, profile, node);
	}

	public static ConfigValue get(EntityPlayerMP player, Node node)
	{
		Preconditions.checkNotNull(player, "Player can't be null!");
		Preconditions.checkNotNull(node, "Config node can't be null!");
		return get(player.server, player.getGameProfile(), node);
	}

	public static ConfigValue getConfigValue(Node node, boolean op)
	{
		RankConfigValueInfo info = getHandler().getInfo(node);
		return info == null ? ConfigNull.INSTANCE : op ? info.defaultOPValue : info.defaultValue;
	}
}