package com.feed_the_beast.ftblib.lib.config;

import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.server.permission.context.IContext;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public interface IRankConfigHandler
{
	ConfigValue getConfigValue(MinecraftServer server, GameProfile profile, Node node, @Nullable IContext context);
}