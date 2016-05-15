package com.feed_the_beast.ftbl.api;

import net.minecraft.world.WorldServer;

/**
 * Created by LatvianModder on 16.02.2016.
 */
public interface IWorldTick
{
	void onTick(WorldServer w, long now);
}
