package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.context.IContext;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author LatvianModder
 */
public interface IRankConfigHandler
{
	void registerRankConfig(RankConfigValueInfo info);

	Collection<RankConfigValueInfo> getRegisteredConfigs();

	ConfigValue getConfigValue(MinecraftServer server, GameProfile profile, Node node, @Nullable IContext context);

	@Nullable
	RankConfigValueInfo getInfo(Node node);
}