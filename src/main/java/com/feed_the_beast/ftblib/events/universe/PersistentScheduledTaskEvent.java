package com.feed_the_beast.ftblib.events.universe;

import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Cancelable;

/**
 * @author LatvianModder
 */
@Cancelable
public class PersistentScheduledTaskEvent extends UniverseEvent
{
	private final ResourceLocation id;
	private final NBTTagCompound data;

	public PersistentScheduledTaskEvent(Universe universe, ResourceLocation i, NBTTagCompound d)
	{
		super(universe);
		id = i;
		data = d;
	}

	public ResourceLocation getID()
	{
		return id;
	}

	public NBTTagCompound getData()
	{
		return data;
	}
}