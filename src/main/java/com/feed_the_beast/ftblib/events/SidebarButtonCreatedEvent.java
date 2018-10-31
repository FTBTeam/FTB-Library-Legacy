package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.client.SidebarButton;

/**
 * @author LatvianModder
 */
public class SidebarButtonCreatedEvent extends FTBLibEvent
{
	private final SidebarButton button;

	public SidebarButtonCreatedEvent(SidebarButton b)
	{
		button = b;
	}

	public SidebarButton getButton()
	{
		return button;
	}
}