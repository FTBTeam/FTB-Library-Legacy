package com.feed_the_beast.ftbl.api.universe;

import com.feed_the_beast.ftbl.api.IUniverse;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public class ForgeUniverseSavedEvent extends ForgeUniverseEvent
{
	private NBTTagCompound data;

	public ForgeUniverseSavedEvent(IUniverse universe, NBTTagCompound nbt)
	{
		super(universe);
		data = nbt;
	}

	public void setData(ResourceLocation id, NBTTagCompound nbt)
	{
		data.setTag(id.toString(), nbt);
	}
}