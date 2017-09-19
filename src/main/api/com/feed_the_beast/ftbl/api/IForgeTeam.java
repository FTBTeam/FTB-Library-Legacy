package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import net.minecraft.util.IStringSerializable;

import java.util.Collection;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public interface IForgeTeam extends IStringSerializable
{
	boolean isValid();

	NBTDataStorage getData();

	IForgePlayer getOwner();

	String getTitle();

	String getDesc();

	EnumTeamColor getColor();

	EnumTeamStatus getHighestStatus(UUID playerId);

	boolean hasStatus(UUID playerId, EnumTeamStatus status);

	default EnumTeamStatus getHighestStatus(IForgePlayer player)
	{
		return getHighestStatus(player.getId());
	}

	default boolean hasStatus(IForgePlayer player, EnumTeamStatus status)
	{
		return hasStatus(player.getId(), status);
	}

	void setStatus(UUID playerId, EnumTeamStatus status);

	Collection<IForgePlayer> getPlayersWithStatus(Collection<IForgePlayer> c, EnumTeamStatus status);

	boolean addPlayer(IForgePlayer player);

	boolean removePlayer(IForgePlayer player);

	void changeOwner(IForgePlayer player);

	ConfigGroup getSettings();

	boolean freeToJoin();
}