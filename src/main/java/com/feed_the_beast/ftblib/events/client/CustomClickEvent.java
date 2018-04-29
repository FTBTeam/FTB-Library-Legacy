package com.feed_the_beast.ftblib.events.client;

import com.feed_the_beast.ftblib.events.FTBLibEvent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author LatvianModder
 */
@Cancelable
public class CustomClickEvent extends FTBLibEvent
{
	private final ResourceLocation id;

	public CustomClickEvent(ResourceLocation _id)
	{
		id = _id;
	}

	public ResourceLocation getID()
	{
		return id;
	}
}