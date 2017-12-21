package com.feed_the_beast.ftblib.events.universe;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

/**
 * @author LatvianModder
 */
public abstract class UniverseLoadedEvent extends UniverseEvent
{
	private WorldServer world;

	public UniverseLoadedEvent(WorldServer w)
	{
		world = w;
	}

	public WorldServer getWorld()
	{
		return world;
	}

	public static class Pre extends UniverseLoadedEvent
	{
		private final NBTTagCompound data;

		public Pre(WorldServer world, NBTTagCompound nbt)
		{
			super(world);
			data = nbt;
		}

		public NBTTagCompound getData(ResourceLocation id)
		{
			return data.getCompoundTag(id.toString());
		}
	}

	public static class Post extends UniverseLoadedEvent
	{
		private final NBTTagCompound data;

		public Post(WorldServer world, NBTTagCompound nbt)
		{
			super(world);
			data = nbt;
		}

		public NBTTagCompound getData(ResourceLocation id)
		{
			return data.getCompoundTag(id.toString());
		}
	}

	public static class Finished extends UniverseLoadedEvent
	{
		public Finished(WorldServer world)
		{
			super(world);
		}
	}
}