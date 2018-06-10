package com.feed_the_beast.ftblib.lib.util.misc;

import com.feed_the_beast.ftblib.lib.data.Universe;

/**
 * @author LatvianModder
 */
public interface IScheduledTask
{
	default boolean isComplete(Universe universe, TimeType type, long time)
	{
		return (type == TimeType.TICKS ? universe.ticks.ticks() : System.currentTimeMillis()) >= time;
	}

	void execute(Universe universe);
}
