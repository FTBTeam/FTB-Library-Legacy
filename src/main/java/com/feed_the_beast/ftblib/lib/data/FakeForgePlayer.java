package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.util.FakePlayer;

/**
 * @author LatvianModder
 */
public class FakeForgePlayer extends ForgePlayer
{
	public FakePlayer playerEntity;

	public FakeForgePlayer(Universe u)
	{
		super(u, ServerUtils.FAKE_PLAYER_PROFILE.getId(), ServerUtils.FAKE_PLAYER_PROFILE.getName());
	}

	@Override
	public boolean isOnline()
	{
		return playerEntity != null;
	}

	@Override
	public EntityPlayerMP getPlayer()
	{
		if (playerEntity == null)
		{
			throw new NullPointerException("Fake player not set yet!");
		}

		return playerEntity;
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

	@Override
	public void markDirty()
	{
		team.universe.markDirty();
	}
}