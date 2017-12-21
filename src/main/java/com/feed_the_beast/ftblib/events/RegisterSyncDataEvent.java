package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.data.ISyncData;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class RegisterSyncDataEvent extends FTBLibEvent
{
	private final BiConsumer<String, ISyncData> callback;

	public RegisterSyncDataEvent(BiConsumer<String, ISyncData> c)
	{
		callback = c;
	}

	public void register(String mod, ISyncData data)
	{
		callback.accept(mod, data);
	}
}