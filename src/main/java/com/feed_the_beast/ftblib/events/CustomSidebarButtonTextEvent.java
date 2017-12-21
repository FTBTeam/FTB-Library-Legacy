package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.client.SidebarButton;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author LatvianModder
 */
@Cancelable
public class CustomSidebarButtonTextEvent extends FTBLibEvent
{
	private final SidebarButton button;
	private String text = "";

	public CustomSidebarButtonTextEvent(SidebarButton b)
	{
		button = b;
	}

	public SidebarButton getButton()
	{
		return button;
	}

	public void setText(String txt)
	{
		text = txt;
	}

	public String getText()
	{
		return text;
	}
}