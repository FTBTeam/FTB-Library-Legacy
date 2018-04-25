package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.data.Action;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterAdminPanelActionsEvent extends FTBLibEvent
{
	private Consumer<Action> callback;

	public RegisterAdminPanelActionsEvent(Consumer<Action> c)
	{
		callback = c;
	}

	public void register(Action action)
	{
		callback.accept(action);
	}
}