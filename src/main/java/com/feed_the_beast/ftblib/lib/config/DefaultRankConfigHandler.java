package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.context.IContext;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LatvianModder
 */
public enum DefaultRankConfigHandler implements IRankConfigHandler
{
	INSTANCE;

	private static final Map<Node, RankConfigValueInfo> MAP = new HashMap<>();
	private static Collection<RankConfigValueInfo> VALUES = Collections.unmodifiableCollection(MAP.values());

	@Override
	public void registerRankConfig(RankConfigValueInfo info)
	{
		Preconditions.checkNotNull(info, "RankConfigValueInfo can't be null!");
		Preconditions.checkArgument(!MAP.containsKey(info.node), "Duplicate rank config ID found: " + info.node);
		MAP.put(info.node, info);
	}

	@Override
	public Collection<RankConfigValueInfo> getRegisteredConfigs()
	{
		return VALUES;
	}

	@Override
	public ConfigValue getConfigValue(MinecraftServer server, GameProfile profile, Node node, @Nullable IContext context)
	{
		RankConfigValueInfo info = RankConfigAPI.getHandler().getInfo(node);

		if (info != null)
		{
			return ServerUtils.isOP(server, profile) ? info.defaultOPValue : info.defaultValue;
		}

		return ConfigNull.INSTANCE;
	}

	@Override
	@Nullable
	public RankConfigValueInfo getInfo(Node node)
	{
		Preconditions.checkNotNull(node, "Config node can't be null!");
		return MAP.get(node);
	}
}