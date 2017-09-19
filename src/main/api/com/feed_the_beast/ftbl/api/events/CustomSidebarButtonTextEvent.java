package com.feed_the_beast.ftbl.api.events;

import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author LatvianModder
 */
@Cancelable
public class CustomSidebarButtonTextEvent extends FTBLibEvent
{
	private final String id;
	private String text = "";

	public CustomSidebarButtonTextEvent(String _id)
	{
		id = _id;
	}

	public String getId()
	{
		return id;
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