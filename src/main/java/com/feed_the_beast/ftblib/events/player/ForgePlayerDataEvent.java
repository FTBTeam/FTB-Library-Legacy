package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.NBTDataStorage;

import java.util.function.Consumer;

/**
 * @author LatvianModder
 */
public class ForgePlayerDataEvent extends ForgePlayerEvent
{
	private final Consumer<NBTDataStorage.Data> callback;

	public ForgePlayerDataEvent(ForgePlayer player, Consumer<NBTDataStorage.Data> c)
	{
		super(player);
		callback = c;
	}

	public void register(NBTDataStorage.Data data)
	{
		callback.accept(data);
	}
}