package com.feed_the_beast.ftbl.api;

import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public interface ISharedData
{
	Side getSide();

	UUID getUniverseID();

	Collection<String> optionalServerMods();
}