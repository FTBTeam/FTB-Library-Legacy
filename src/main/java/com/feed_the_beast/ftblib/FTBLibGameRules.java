package com.feed_the_beast.ftblib;

import net.minecraft.world.World;

/**
 * @author LatvianModder
 */
public interface FTBLibGameRules
{
	String DISABLE_TEAM_CREATION = FTBLib.MOD_ID + ":disable_team_creation";
	String DISABLE_TEAM_JOINING = FTBLib.MOD_ID + ":disable_team_joining";

	static boolean canCreateTeam(World world)
	{
		return !world.getGameRules().getBoolean(DISABLE_TEAM_CREATION);
	}

	static boolean canJoinTeam(World world)
	{
		return !world.getGameRules().getBoolean(DISABLE_TEAM_JOINING);
	}
}