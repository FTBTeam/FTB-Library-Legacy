package com.feed_the_beast.ftblib.lib.data;

import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;

/**
 * @author LatvianModder
 */
public final class ForgePlayerFake extends ForgePlayer
{
	public static final ForgePlayerFake SERVER = new ForgePlayerFake(UUID.nameUUIDFromBytes("FTBLib_Server".getBytes()), "Server");

	public ForgePlayerFake(UUID id, String name)
	{
		super(id, name);
	}

	ForgePlayerFake(FakePlayer p)
	{
		this(p.getGameProfile().getId(), p.getGameProfile().getName());
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