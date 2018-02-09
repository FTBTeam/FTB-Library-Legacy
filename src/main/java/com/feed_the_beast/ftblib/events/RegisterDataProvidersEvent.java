package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.IDataProvider;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public abstract class RegisterDataProvidersEvent<T> extends FTBLibEvent
{
	private final BiConsumer<String, IDataProvider<T>> callback;

	public RegisterDataProvidersEvent(BiConsumer<String, IDataProvider<T>> c)
	{
		callback = c;
	}

	public void register(String id, IDataProvider<T> provider)
	{
		callback.accept(id, provider);
	}

	public static class Player extends RegisterDataProvidersEvent<ForgePlayer>
	{
		public Player(BiConsumer<String, IDataProvider<ForgePlayer>> c)
		{
			super(c);
		}
	}

	public static class Team extends RegisterDataProvidersEvent<ForgeTeam>
	{
		public Team(BiConsumer<String, IDataProvider<ForgeTeam>> c)
		{
			super(c);
		}
	}
}