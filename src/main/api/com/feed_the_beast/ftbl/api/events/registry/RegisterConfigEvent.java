package com.feed_the_beast.ftbl.api.events.registry;

import com.feed_the_beast.ftbl.api.config.IConfigFileProvider;
import com.feed_the_beast.ftbl.api.config.IConfigKey;
import com.feed_the_beast.ftbl.api.config.IConfigValue;
import com.feed_the_beast.ftbl.api.config.IConfigValueProvider;
import com.feed_the_beast.ftbl.api.events.FTBLibEvent;
import com.feed_the_beast.ftbl.lib.TriFunction;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class RegisterConfigEvent extends FTBLibEvent
{
	private final TriFunction<IConfigKey, String, String, IConfigValue> callback;
	private final BiConsumer<String, IConfigFileProvider> fileCallback;
	private final BiConsumer<String, IConfigValueProvider> valueCallback;

	public RegisterConfigEvent(TriFunction<IConfigKey, String, String, IConfigValue> c, BiConsumer<String, IConfigFileProvider> fc, BiConsumer<String, IConfigValueProvider> cv)
	{
		callback = c;
		fileCallback = fc;
		valueCallback = cv;
	}

	public IConfigKey register(String group, String id, IConfigValue value)
	{
		return callback.apply(group, id, value);
	}

	public void registerFile(String id, IConfigFileProvider provider)
	{
		fileCallback.accept(id, provider);
	}

	public void registerValue(String id, IConfigValueProvider provider)
	{
		valueCallback.accept(id, provider);
	}
}