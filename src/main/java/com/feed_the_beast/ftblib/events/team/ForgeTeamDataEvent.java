package com.feed_the_beast.ftblib.events.team;

import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
import com.feed_the_beast.ftblib.lib.data.NBTDataStorage;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class ForgeTeamDataEvent extends ForgeTeamEvent
{
	private final Consumer<NBTDataStorage.Data> callback;

	public ForgeTeamDataEvent(ForgeTeam team, Consumer<NBTDataStorage.Data> c)
	{
		super(team);
		callback = c;
	}

	public void register(NBTDataStorage.Data data)
	{
		callback.accept(data);
	}
}