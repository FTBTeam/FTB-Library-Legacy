package com.feed_the_beast.ftblib.events;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class SyncGamerulesEvent extends FTBLibEvent
{
	private final Consumer<String> callback;

	public SyncGamerulesEvent(Consumer<String> c)
	{
		callback = c;
	}

	public void sync(String gamerule)
	{
		callback.accept(gamerule);
	}
}