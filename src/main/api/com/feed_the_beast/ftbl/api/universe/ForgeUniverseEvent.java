package com.feed_the_beast.ftbl.api.universe;

import com.feed_the_beast.ftbl.api.FTBLibEvent;
import com.feed_the_beast.ftbl.api.IUniverse;

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