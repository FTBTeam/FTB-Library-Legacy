package com.feed_the_beast.ftbl.api.events;

import net.minecraftforge.fml.common.LoaderState;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * @author LatvianModder
 */
public class ConfigLoadedEvent extends Event
{
	private final LoaderState.ModState state;

	public ConfigLoadedEvent(LoaderState.ModState s)
	{
		state = s;
	}

	public LoaderState.ModState getState()
	{
		return state;
	}
}