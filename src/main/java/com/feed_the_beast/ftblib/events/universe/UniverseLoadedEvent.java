package com.feed_the_beast.ftblib.events.universe;

import com.feed_the_beast.ftblib.lib.data.Universe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;

/**
 * @author LatvianModder
 */
public abstract class UniverseLoadedEvent extends UniverseEvent
{
	private final WorldServer world;

	public UniverseLoadedEvent(Universe universe, WorldServer w)
	{
		super(universe);
		world = w;
	}

	public WorldServer getWorld()
	{
		return world;
	}

	public static class Pre extends UniverseLoadedEvent
	{
		private final NBTTagCompound data;

		public Pre(Universe universe, WorldServer world, NBTTagCompound nbt)
		{
			super(universe, world);
			data = nbt;
		}

		public NBTTagCompound getData(String id)
		{
			NBTTagCompound tag = data.getCompoundTag(id);
			return tag.hasNoTags() ? data.getCompoundTag(id + ":data") : tag;
		}
	}

	public static class CreateServerTeams extends UniverseLoadedEvent
	{
		public CreateServerTeams(Universe universe, WorldServer world)
		{
			super(universe, world);
		}
	}

	public static class Post extends UniverseLoadedEvent
	{
		private final NBTTagCompound data;

		public Post(Universe universe, WorldServer world, NBTTagCompound nbt)
		{
			super(universe, world);
			data = nbt;
		}

		public NBTTagCompound getData(String id)
		{
			NBTTagCompound tag = data.getCompoundTag(id);
			return tag.hasNoTags() ? data.getCompoundTag(id + ":data") : tag;
		}
	}

	public static class Finished extends UniverseLoadedEvent
	{
		public Finished(Universe universe, WorldServer world)
		{
			super(universe, world);
		}
	}
}