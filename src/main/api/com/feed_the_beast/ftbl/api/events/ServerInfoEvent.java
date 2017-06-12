package com.feed_the_beast.ftbl.api.events;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.lib.guide.GuidePage;
import net.minecraftforge.fml.common.eventhandler.Event;

public class ServerInfoEvent extends Event
{
	private final GuidePage file;
	private final IForgePlayer player;
	private final boolean isOP;

	public ServerInfoEvent(GuidePage f, IForgePlayer p, boolean o)
	{
		file = f;
		player = p;
		isOP = o;
	}

	public GuidePage getFile()
	{
		return file;
	}

	public IForgePlayer getPlayer()
	{
		return player;
	}

	public boolean isOP()
	{
		return isOP;
	}
}