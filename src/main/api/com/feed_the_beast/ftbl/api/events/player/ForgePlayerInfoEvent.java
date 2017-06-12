package com.feed_the_beast.ftbl.api.events.player;

import com.feed_the_beast.ftbl.api.IForgePlayer;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author LatvianModder
 */
public class ForgePlayerInfoEvent extends ForgePlayerEvent
{
	private final List<ITextComponent> list;
	private final long currentTime;

	public ForgePlayerInfoEvent(IForgePlayer player, List<ITextComponent> l, long t)
	{
		super(player);
		list = l;
		currentTime = t;
	}

	public List<ITextComponent> getInfo()
	{
		return list;
	}

	public long getCurrentTime()
	{
		return currentTime;
	}
}