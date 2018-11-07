package com.feed_the_beast.ftblib.lib.data;

/**
 * @author LatvianModder
 */
public abstract class PlayerData implements NBTDataStorage.Data
{
	public final ForgePlayer player;

	public PlayerData(ForgePlayer p)
	{
		player = p;
	}

	@Override
	public final int hashCode()
	{
		return getID().hashCode() * 31 + player.hashCode();
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
			return player.equalsPlayer(((PlayerData) o).player) && getID().equals(((PlayerData) o).getID());
		}

		return false;
	}

	public final String toString()
	{
		return player.getName() + ':' + getID();
	}
}