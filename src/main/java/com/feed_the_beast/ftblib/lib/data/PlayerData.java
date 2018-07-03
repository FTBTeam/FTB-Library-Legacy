package com.feed_the_beast.ftblib.lib.data;

/**
 * @author LatvianModder
 */
public abstract class PlayerData extends NBTDataStorage.Data
{
	public final ForgePlayer player;

	public PlayerData(ForgePlayer p)
	{
		player = p;
	}

	@Override
	public final int hashCode()
	{
		return getName().hashCode() * 31 + player.hashCode();
	}

	@Override
	public final boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof PlayerData)
		{
			return player.equalsPlayer(((PlayerData) o).player) && getName().equals(((PlayerData) o).getName());
		}

		return false;
	}

	public final String toString()
	{
		return player.getName() + ':' + getName();
	}
}