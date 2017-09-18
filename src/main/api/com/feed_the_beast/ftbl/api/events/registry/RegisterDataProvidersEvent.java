package com.feed_the_beast.ftbl.api.events.registry;

import com.feed_the_beast.ftbl.api.IDataProvider;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.events.FTBLibEvent;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public abstract class RegisterDataProvidersEvent<T> extends FTBLibEvent
{
	private final BiConsumer<ResourceLocation, IDataProvider<T>> callback;

	public RegisterDataProvidersEvent(BiConsumer<ResourceLocation, IDataProvider<T>> c)
	{
		callback = c;
	}

	public void register(ResourceLocation id, IDataProvider<T> provider)
	{
		callback.accept(id, provider);
	}

	public static class Player extends RegisterDataProvidersEvent<IForgePlayer>
	{
		public Player(BiConsumer<ResourceLocation, IDataProvider<IForgePlayer>> c)
		{
			super(c);
		}
	}

	public static class Team extends RegisterDataProvidersEvent<IForgeTeam>
	{
		public Team(BiConsumer<ResourceLocation, IDataProvider<IForgeTeam>> c)
		{
			super(c);
		}
	}
}