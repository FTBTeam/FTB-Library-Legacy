package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.RankConfigValueInfo;
import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.feed_the_beast.ftblib.lib.util.misc.TriFunction;

/**
 * @author LatvianModder
 */
public class RegisterRankConfigEvent extends FTBLibEvent
{
	private final TriFunction<RankConfigValueInfo, Node, ConfigValue, ConfigValue> callback;

	public RegisterRankConfigEvent(TriFunction<RankConfigValueInfo, Node, ConfigValue, ConfigValue> c)
	{
		callback = c;
	}

	public RankConfigValueInfo register(Node id, ConfigValue defPlayer, ConfigValue defOP)
	{
		return callback.apply(id, defPlayer, defOP);
	}
}