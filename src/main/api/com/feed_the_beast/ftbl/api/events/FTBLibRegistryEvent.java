package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.IFTBLibRegistry;

/**
 * @author LatvianModder
 */
public class FTBLibRegistryEvent extends FTBLibEvent
{
	private final IFTBLibRegistry reg;

	public FTBLibRegistryEvent(IFTBLibRegistry r)
	{
		reg = r;
	}

	public IFTBLibRegistry getRegistry()
	{
		return reg;
	}
}