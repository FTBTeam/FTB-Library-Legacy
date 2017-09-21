package com.feed_the_beast.ftbl.api.events;

import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

/**
 * @author LatvianModder
 */
public class PermissionRegistryEvent extends FTBLibEvent
{
	public void registerNode(String node, DefaultPermissionLevel level, String description)
	{
		PermissionAPI.registerNode(node, level, description);
	}

	public void registerNode(String node, DefaultPermissionLevel level)
	{
		registerNode(node, level, "");
	}
}