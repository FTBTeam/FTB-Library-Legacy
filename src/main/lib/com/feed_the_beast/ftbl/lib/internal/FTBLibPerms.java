package com.feed_the_beast.ftbl.lib.internal;

import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.PermissionRegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.server.permission.DefaultPermissionLevel;

/**
 * @author LatvianModder
 */
@EventHandler
public class FTBLibPerms
{
	public static final String SHOW_OP_BUTTONS = "ftbl.op_buttons";

	@SubscribeEvent
	public static void registerPermissions(PermissionRegistryEvent event)
	{
		event.registerNode(SHOW_OP_BUTTONS, DefaultPermissionLevel.OP, "Node for displaying OP-specific buttons in inventory");
	}
}