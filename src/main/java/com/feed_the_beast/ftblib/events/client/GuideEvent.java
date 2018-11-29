package com.feed_the_beast.ftblib.events.client;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author LatvianModder
 */
@Cancelable
public class GuideEvent extends FTBLibEvent
{
	private final String path;

	private GuideEvent(String p)
	{
		path = p;
	}

	public static class Check extends GuideEvent
	{
		private Check(String path)
		{
			super(path);
		}
	}

	public static class Open extends GuideEvent
	{
		private Open(String path)
		{
			super(path);
		}
	}

	public String getPath()
	{
		return path;
	}
}