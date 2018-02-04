package com.feed_the_beast.ftblib.lib.data;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public final class ForgePlayerFake extends ForgePlayer
{
	public ForgePlayerFake(Universe u, UUID id, String name)
	{
		super(u, id, name);
	}

	@Override
	public boolean isFake()
	{
		return true;
	}

	@Override
	public boolean isOP()
	{
		return false;
	}
}