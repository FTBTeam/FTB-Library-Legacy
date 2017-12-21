package com.feed_the_beast.ftblib.events;

import com.feed_the_beast.ftblib.lib.IDataProvider;
import com.feed_the_beast.ftblib.lib.data.ForgePlayer;
import com.feed_the_beast.ftblib.lib.data.ForgeTeam;
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

	public static class Player extends RegisterDataProvidersEvent<ForgePlayer>
	{
		public Player(BiConsumer<ResourceLocation, IDataProvider<ForgePlayer>> c)
		{
			super(c);
		}
	}

	public static class Team extends RegisterDataProvidersEvent<ForgeTeam>
	{
		public Team(BiConsumer<ResourceLocation, IDataProvider<ForgeTeam>> c)
		{
			super(c);
		}
	}
}