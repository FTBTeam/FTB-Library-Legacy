package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.api.ISharedData;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public abstract class SharedData implements ISharedData
{
	public UUID universeId;
	public final Collection<String> optionalServerMods = new HashSet<>();

	SharedData()
	{
	}

	public void reset()
	{
		universeId = null;
	}

	@Override
	public UUID getUniverseId()
	{
		if (universeId == null || (universeId.getLeastSignificantBits() == 0L && universeId.getMostSignificantBits() == 0L))
		{
			universeId = UUID.randomUUID();
		}

		return universeId;
	}

	@Override
	public Collection<String> optionalServerMods()
	{
		return optionalServerMods;
	}
}