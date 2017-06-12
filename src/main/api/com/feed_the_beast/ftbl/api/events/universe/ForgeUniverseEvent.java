package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public abstract class ForgeUniverseEvent extends Event
{
	private final IUniverse universe;

	public ForgeUniverseEvent(IUniverse u)
	{
		universe = u;
	}

	public IUniverse getUniverse()
	{
		return universe;
	}
}