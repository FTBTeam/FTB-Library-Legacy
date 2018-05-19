package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.data.AdminPanelAction;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterAdminPanelActionsEvent extends FTBLibEvent
{
	private Consumer<AdminPanelAction> callback;

	public RegisterAdminPanelActionsEvent(Consumer<AdminPanelAction> c)
	{
		callback = c;
	}

	public void register(AdminPanelAction action)
	{
		callback.accept(action);
	}
}