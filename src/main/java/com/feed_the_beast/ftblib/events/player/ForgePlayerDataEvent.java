package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.PlayerData;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class ForgePlayerDataEvent extends ForgePlayerEvent
{
	private final Consumer<PlayerData> callback;

	public ForgePlayerDataEvent(ForgePlayer player, Consumer<PlayerData> c)
	{
		super(player);
		callback = c;
	}

	public void register(PlayerData data)
	{
		callback.accept(data);
	}
}