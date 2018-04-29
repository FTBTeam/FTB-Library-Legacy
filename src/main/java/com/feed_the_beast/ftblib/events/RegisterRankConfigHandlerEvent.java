package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.config.IRankConfigHandler;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterRankConfigHandlerEvent extends FTBLibEvent
{
	private final Consumer<IRankConfigHandler> callback;

	public RegisterRankConfigHandlerEvent(Consumer<IRankConfigHandler> c)
	{
		callback = c;
	}

	public void setHandler(IRankConfigHandler handler)
	{
		callback.accept(handler);
	}
}