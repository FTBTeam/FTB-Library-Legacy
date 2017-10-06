package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.RankConfigValueInfo;
import com.feed_the_beast.ftbl.lib.util.misc.TriFunction;

/**
 * @author LatvianModder
 */
public class RegisterRankConfigEvent extends FTBLibEvent
{
	private final TriFunction<RankConfigValueInfo, String, ConfigValue, ConfigValue> callback;

	public RegisterRankConfigEvent(TriFunction<RankConfigValueInfo, String, ConfigValue, ConfigValue> c)
	{
		callback = c;
	}

	public RankConfigValueInfo register(String id, ConfigValue defPlayer, ConfigValue defOP)
	{
		return callback.apply(id, defPlayer, defOP);
	}
}