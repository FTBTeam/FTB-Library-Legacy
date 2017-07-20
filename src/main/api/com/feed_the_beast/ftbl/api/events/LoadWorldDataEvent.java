package com.feed_the_beast.ftbl.api.events;

import net.minecraft.server.MinecraftServer;

/**
 * @author LatvianModder
 */
public class LoadWorldDataEvent extends FTBLibEvent
{
	private final MinecraftServer server;

	public LoadWorldDataEvent(MinecraftServer s)
	{
		server = s;
	}

	public MinecraftServer getServer()
	{
		return server;
	}
}