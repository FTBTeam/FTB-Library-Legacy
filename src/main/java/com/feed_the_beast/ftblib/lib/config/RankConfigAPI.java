package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.events.RegisterRankConfigEvent;
import com.feed_the_beast.ftblib.events.RegisterRankConfigHandlerEvent;
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

	private static void setHandler(IRankConfigHandler h)
	{
		Preconditions.checkNotNull(h, "Permission handler can't be null!");

		if (handler != null)
		{
			FTBLib.LOGGER.warn("Replacing " + handler.getClass().getName() + " with " + h.getClass().getName());
		}

		handler = h;
	}

	public static IRankConfigHandler getHandler()
	{
		if (handler == null)
		{
			new RegisterRankConfigHandlerEvent(RankConfigAPI::setHandler).post();

			if (handler == null)
			{
				handler = DefaultRankConfigHandler.INSTANCE;
			}

			new RegisterRankConfigEvent(handler::registerRankConfig).post();
		}

		return handler;
	}

	public static ConfigValue get(MinecraftServer server, GameProfile profile, Node node, @Nullable IContext context)
	{
		Preconditions.checkNotNull(profile, "GameProfile can't be null!");
		Preconditions.checkNotNull(node, "Config node can't be null!");
		return getHandler().getConfigValue(server, profile, node, context);
	}

	public static ConfigValue get(EntityPlayerMP player, Node node)
	{
		Preconditions.checkNotNull(player, "Player can't be null!");
		Preconditions.checkNotNull(node, "Config node can't be null!");
		return get(player.mcServer, player.getGameProfile(), node, new PlayerContext(player));
	}

	public static ConfigValue getConfigValue(Node node, boolean op)
	{
		RankConfigValueInfo info = getHandler().getInfo(node);
		return info == null ? ConfigNull.INSTANCE : op ? info.defaultOPValue : info.defaultValue;
	}
}