package com.feed_the_beast.ftblib.lib.data;

/**
 * @author LatvianModder
 */
public abstract class TeamData extends NBTDataStorage.Data
{
	public final ForgeTeam team;

	public TeamData(ForgeTeam t)
	{
		team = t;
	}

	@Override
	public final int hashCode()
	{
		return getName().hashCode() * 31 + team.hashCode();
	}

	@Override
	public final boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}
		else if (o instanceof TeamData)
		{
			return team.equalsTeam(((TeamData) o).team) && getName().equals(((TeamData) o).getName());
		}

		return false;
	}

	public final String toString()
	{
		return team.getName() + ':' + getName();
	}
}