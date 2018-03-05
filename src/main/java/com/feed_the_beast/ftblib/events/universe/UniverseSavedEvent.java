package com.feed_the_beast.ftblib.events.universe;

import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author LatvianModder
 */
public class UniverseSavedEvent extends UniverseEvent
{
	private NBTTagCompound data;

	public UniverseSavedEvent(Universe universe, NBTTagCompound nbt)
	{
		super(universe);
		data = nbt;
	}

	public void setData(String id, NBTTagCompound nbt)
	{
		data.setTag(id, nbt);
	}
}