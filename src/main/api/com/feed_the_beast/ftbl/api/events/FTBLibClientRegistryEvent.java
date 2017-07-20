package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.IFTBLibClientRegistry;

/**
 * @author LatvianModder
 */
public class FTBLibClientRegistryEvent extends FTBLibEvent
{
	private final IFTBLibClientRegistry reg;

	public FTBLibClientRegistryEvent(IFTBLibClientRegistry r)
	{
		reg = r;
	}

	public IFTBLibClientRegistry getRegistry()
	{
		return reg;
	}
}