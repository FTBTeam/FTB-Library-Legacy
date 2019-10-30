package com.feed_the_beast.ftblib;

import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public interface FTBLibGameRules
{
	String DISABLE_TEAM_CREATION = "ftblib:disable_team_creation";
	String DISABLE_TEAM_JOINING = "ftblib:disable_team_joining";

	static boolean canCreateTeam(World world)
	{
		return !FTBLibConfig.teams.disable_teams && !world.getGameRules().getBoolean(DISABLE_TEAM_CREATION);
	}

	static boolean canJoinTeam(World world)
	{
		return !FTBLibConfig.teams.disable_teams && !world.getGameRules().getBoolean(DISABLE_TEAM_JOINING);
	}
}