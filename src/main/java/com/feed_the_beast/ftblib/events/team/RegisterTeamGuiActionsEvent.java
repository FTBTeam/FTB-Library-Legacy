package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import com.feed_the_beast.ftblib.lib.data.TeamGuiAction;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class RegisterTeamGuiActionsEvent extends FTBLibEvent
{
	private Consumer<TeamGuiAction> callback;

	public RegisterTeamGuiActionsEvent(Consumer<TeamGuiAction> c)
	{
		callback = c;
	}

	public void register(TeamGuiAction action)
	{
		callback.accept(action);
	}
}