package com.feed_the_beast.ftbl.api.universe;

import com.feed_the_beast.ftbl.api.IUniverse;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
 * @author LatvianModder
 */
public abstract class ForgeUniverseLoadedEvent extends ForgeUniverseEvent
{
	public ForgeUniverseLoadedEvent(IUniverse universe)
	{
		super(universe);
	}

	public static class Pre extends ForgeUniverseLoadedEvent
	{
		private final NBTTagCompound data;

		public Pre(IUniverse universe, NBTTagCompound nbt)
		{
			super(universe);
			data = nbt;
		}

		public NBTTagCompound getData(ResourceLocation id)
		{
			return data.getCompoundTag(id.toString());
		}
	}

	public static class Post extends ForgeUniverseLoadedEvent
	{
		private final NBTTagCompound data;

		public Post(IUniverse universe, NBTTagCompound nbt)
		{
			super(universe);
			data = nbt;
		}

		public NBTTagCompound getData(ResourceLocation id)
		{
			return data.getCompoundTag(id.toString());
		}
	}

	public static class Finished extends ForgeUniverseLoadedEvent
	{
		public Finished(IUniverse universe)
		{
			super(universe);
		}
	}
}