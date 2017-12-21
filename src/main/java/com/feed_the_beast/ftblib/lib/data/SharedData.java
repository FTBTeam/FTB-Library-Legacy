package com.feed_the_beast.ftblib.lib.data;

import net.minecraftforge.fml.relauncher.Side;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public abstract class SharedData
{
	public UUID universeId;
	public final Collection<String> optionalServerMods = new HashSet<>();

	SharedData()
	{
	}

	public abstract Side getSide();

	public void reset()
	{
		universeId = null;
	}

	public UUID getUniverseId()
	{
		if (universeId == null || (universeId.getLeastSignificantBits() == 0L && universeId.getMostSignificantBits() == 0L))
		{
			universeId = UUID.randomUUID();
		}

		return universeId;
	}

	public Collection<String> optionalServerMods()
	{
		return optionalServerMods;
	}
}