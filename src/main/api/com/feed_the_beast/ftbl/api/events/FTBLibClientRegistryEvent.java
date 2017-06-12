package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.IFTBLibClientRegistry;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public class FTBLibClientRegistryEvent extends Event
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