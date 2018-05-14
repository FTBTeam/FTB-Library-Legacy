package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.data.Universe;

/**
 * @author LatvianModder
 */
public interface IScheduledTask
{
	default boolean isComplete(long now, long time)
	{
		return now >= time;
	}

	void execute(Universe universe);
}
