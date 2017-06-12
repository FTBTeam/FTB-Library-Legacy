package com.feed_the_beast.ftbl.api.events;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public class LoadWorldDataEvent extends Event
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