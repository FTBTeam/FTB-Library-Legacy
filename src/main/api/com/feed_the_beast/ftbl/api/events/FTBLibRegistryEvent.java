package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.IFTBLibRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public class FTBLibRegistryEvent extends Event
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