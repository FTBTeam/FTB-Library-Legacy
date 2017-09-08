package com.feed_the_beast.ftbl.api.events.registry;

import com.feed_the_beast.ftbl.api.events.FTBLibEvent;
import com.feed_the_beast.ftbl.lib.TriFunction;
import com.feed_the_beast.ftbl.lib.config.ConfigValue;
import com.feed_the_beast.ftbl.lib.config.RankConfigKey;

/**
 * @author LatvianModder
 */
public class RegisterRankConfigEvent extends FTBLibEvent
{
	private final TriFunction<RankConfigKey, String, ConfigValue, ConfigValue> callback;

	public RegisterRankConfigEvent(TriFunction<RankConfigKey, String, ConfigValue, ConfigValue> c)
	{
		callback = c;
	}

	public RankConfigKey register(String id, ConfigValue defPlayer, ConfigValue defOP)
	{
		return callback.apply(id, defPlayer, defOP);
	}
}