package com.feed_the_beast.ftbl.api.player;

import com.feed_the_beast.ftbl.api.FTBLibEvent;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class RegisterContainerProvidersEvent extends FTBLibEvent
{
	private final BiConsumer<ResourceLocation, IContainerProvider> callback;

	public RegisterContainerProvidersEvent(BiConsumer<ResourceLocation, IContainerProvider> c)
	{
		callback = c;
	}

	public void register(ResourceLocation id, IContainerProvider provider)
	{
		callback.accept(id, provider);
	}
}