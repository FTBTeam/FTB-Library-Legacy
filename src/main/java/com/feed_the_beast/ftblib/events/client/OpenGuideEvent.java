package com.feed_the_beast.ftblib.events.client;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author LatvianModder
 */
@Cancelable
public class OpenGuideEvent extends FTBLibEvent
{
	private final String path;

	public OpenGuideEvent(String p)
	{
		path = p;
	}

	public OpenGuideEvent()
	{
		this("");
	}

	public String getPath()
	{
		return path;
	}

	public boolean checkingIfGuideExists()
	{
		return path.isEmpty();
	}
}