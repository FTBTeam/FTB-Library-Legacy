package com.feed_the_beast.ftblib.events.universe;

import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

/**
 * @author LatvianModder
 */
public abstract class UniverseLoadedEvent extends UniverseEvent
{
	private WorldServer world;

	public UniverseLoadedEvent(Universe u, WorldServer w)
	{
		super(u);
		world = w;
	}

	public WorldServer getWorld()
	{
		return world;
	}

	public static class Pre extends UniverseLoadedEvent
	{
		private final NBTTagCompound data;

		public Pre(Universe u, WorldServer world, NBTTagCompound nbt)
		{
			super(u, world);
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

		public Post(Universe u, WorldServer world, NBTTagCompound nbt)
		{
			super(u, world);
			data = nbt;
		}

		public NBTTagCompound getData(ResourceLocation id)
		{
			return data.getCompoundTag(id.toString());
		}
	}

	public static class Finished extends UniverseLoadedEvent
	{
		public Finished(Universe u, WorldServer world)
		{
			super(u, world);
		}
	}
}