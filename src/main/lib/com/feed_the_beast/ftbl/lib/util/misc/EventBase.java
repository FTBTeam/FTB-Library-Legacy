package com.feed_the_beast.ftbl.lib.util.misc;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public class EventBase extends Event
{
	private boolean canPost = true;

	public boolean post()
	{
		if (canPost)
		{
			canPost = false;
			return MinecraftForge.EVENT_BUS.post(this);
		}

		return false;
	}
}