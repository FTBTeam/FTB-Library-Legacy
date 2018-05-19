package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import com.feed_the_beast.ftblib.lib.data.TeamAction;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterTeamGuiActionsEvent extends FTBLibEvent
{
	private Consumer<TeamAction> callback;

	public RegisterTeamGuiActionsEvent(Consumer<TeamAction> c)
	{
		callback = c;
	}

	public void register(TeamAction action)
	{
		callback.accept(action);
	}
}