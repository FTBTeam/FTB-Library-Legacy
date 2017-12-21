package com.feed_the_beast.ftblib.events.player;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;

/**
 * @author LatvianModder
 */
public class RegisterGuiProvidersEvent extends FTBLibEvent
{
	private final BiConsumer<ResourceLocation, IGuiProvider> callback;

	public RegisterGuiProvidersEvent(BiConsumer<ResourceLocation, IGuiProvider> c)
	{
		callback = c;
	}

	public void register(ResourceLocation id, IGuiProvider provider)
	{
		callback.accept(id, provider);
	}
}