package com.feed_the_beast.ftbl.api.team;

import com.feed_the_beast.ftbl.api.FTBLibEvent;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterTeamGuiActionsEvent extends FTBLibEvent
{
	private Consumer<ITeamGuiAction> callback;

	public RegisterTeamGuiActionsEvent(Consumer<ITeamGuiAction> c)
	{
		callback = c;
	}

	public void register(ITeamGuiAction action)
	{
		callback.accept(action);
	}
}