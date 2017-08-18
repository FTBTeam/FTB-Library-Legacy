package com.feed_the_beast.ftbl.api.events.registry;

import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.events.FTBLibEvent;
import com.feed_the_beast.ftbl.lib.TriFunction;

/**
 * @author LatvianModder
 */
public class RegisterClientConfigEvent extends FTBLibEvent
{
	private final TriFunction<IConfigKey, String, String, IConfigValue> callback;

	public RegisterClientConfigEvent(TriFunction<IConfigKey, String, String, IConfigValue> c)
	{
		callback = c;
	}

	public IConfigKey register(String group, String id, IConfigValue value)
	{
		return callback.apply(group, id, value);
	}
}