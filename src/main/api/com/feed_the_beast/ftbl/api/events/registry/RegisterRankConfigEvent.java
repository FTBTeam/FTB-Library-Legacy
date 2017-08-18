package com.feed_the_beast.ftbl.api.events.registry;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.events.FTBLibEvent;
import com.feed_the_beast.ftbl.lib.TriFunction;

/**
 * @author LatvianModder
 */
public class RegisterRankConfigEvent extends FTBLibEvent
{
	private final TriFunction<IConfigKey, String, IConfigValue, IConfigValue> callback;

	public RegisterRankConfigEvent(TriFunction<IConfigKey, String, IConfigValue, IConfigValue> c)
	{
		callback = c;
	}

	public IConfigKey register(String id, IConfigValue defPlayer, IConfigValue defOP)
	{
		return callback.apply(id, defPlayer, defOP);
	}
}