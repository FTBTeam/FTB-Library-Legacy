package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.events.FTBLibEvent;

/**
 * @author LatvianModder
 */
public abstract class ForgeUniverseEvent extends FTBLibEvent
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