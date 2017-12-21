package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.config.ConfigValueProvider;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class RegisterConfigValueProvidersEvent extends FTBLibEvent
{
	private final BiConsumer<String, ConfigValueProvider> callback;

	public RegisterConfigValueProvidersEvent(BiConsumer<String, ConfigValueProvider> c)
	{
		callback = c;
	}

	public void register(String id, ConfigValueProvider provider)
	{
		callback.accept(id, provider);
	}
}