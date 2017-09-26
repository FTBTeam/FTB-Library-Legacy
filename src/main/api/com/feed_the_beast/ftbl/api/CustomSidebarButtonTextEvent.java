package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author LatvianModder
 */
@Cancelable
public class CustomSidebarButtonTextEvent extends FTBLibEvent
{
	private final ISidebarButton button;
	private String text = "";

	public CustomSidebarButtonTextEvent(ISidebarButton b)
	{
		button = b;
	}

	public ISidebarButton getButton()
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