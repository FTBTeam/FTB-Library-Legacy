package com.feed_the_beast.ftbl.api.events.registry;

import com.feed_the_beast.ftbl.api.events.FTBLibEvent;
import com.feed_the_beast.ftbl.api.guide.IGuideTextLineProvider;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class RegisterGuideLineProvidersEvent extends FTBLibEvent
{
	private final BiConsumer<String, IGuideTextLineProvider> callback;

	public RegisterGuideLineProvidersEvent(BiConsumer<String, IGuideTextLineProvider> c)
	{
		callback = c;
	}

	public void register(String mod, IGuideTextLineProvider data)
	{
		callback.accept(mod, data);
	}
}