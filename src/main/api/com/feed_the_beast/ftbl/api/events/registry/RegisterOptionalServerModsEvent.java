package com.feed_the_beast.ftbl.api.events.registry;

import com.feed_the_beast.ftbl.api.events.FTBLibEvent;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterOptionalServerModsEvent extends FTBLibEvent
{
	private final Consumer<String> callback;

	public RegisterOptionalServerModsEvent(Consumer<String> c)
	{
		callback = c;
	}

	public void register(String mod)
	{
		callback.accept(mod);
	}
}