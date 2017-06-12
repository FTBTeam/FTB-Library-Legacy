package com.feed_the_beast.ftbl.api.events.universe;

import com.feed_the_beast.ftbl.api.IUniverse;

/**
 * @author LatvianModder
 */
public class ForgeUniversePostLoadedEvent extends ForgeUniverseEvent
{
	public ForgeUniversePostLoadedEvent(IUniverse universe)
	{
		super(universe);
	}
}