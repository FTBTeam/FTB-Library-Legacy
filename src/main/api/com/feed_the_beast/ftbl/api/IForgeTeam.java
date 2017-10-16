package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.util.misc.NBTDataStorage;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.server.permission.PermissionAPI;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author LatvianModder
 */
public interface IForgeTeam extends IStringSerializable
{
	boolean isValid();

	default boolean equalsTeam(@Nullable IForgeTeam team)
	{
		return team == this || (team != null && getName().equals(team.getName()));
	}

	NBTDataStorage getData();

	IForgePlayer getOwner();

	String getTitle();

	String getDesc();

	EnumTeamColor getColor();

	default EnumTeamStatus getHighestStatus(@Nullable IForgePlayer player)
	{
		if (player == null)
		{
			return EnumTeamStatus.NONE;
		}
		else if (isOwner(player))
		{
			return EnumTeamStatus.OWNER;
		}
		else if (isModerator(player))
		{
			return EnumTeamStatus.MOD;
		}
		else if (isMember(player))
		{
			return EnumTeamStatus.MEMBER;
		}
		else if (isEnemy(player))
		{
			return EnumTeamStatus.ENEMY;
		}
		else if (isAlly(player))
		{
			return EnumTeamStatus.ALLY;
		}
		else if (isInvited(player))
		{
			return EnumTeamStatus.INVITED;
		}

		return EnumTeamStatus.NONE;
	}

	default boolean hasStatus(@Nullable IForgePlayer player, EnumTeamStatus status)
	{
		if (player == null)
		{
			return false;
		}

		switch (status)
		{
			case NONE:
				return true;
			case ENEMY:
				return isEnemy(player);
			case REQUESTING_INVITE:
				return isRequestingInvite(player);
			case ALLY:
				return isAlly(player);
			case INVITED:
				return isInvited(player);
			case MEMBER:
				return isMember(player);
			case MOD:
				return isModerator(player);
			case OWNER:
				return isOwner(player);
			default:
				return false;
		}
	}

	boolean setStatus(IForgePlayer player, EnumTeamStatus status);

	default Collection<IForgePlayer> getPlayersWithStatus(Collection<IForgePlayer> collection, EnumTeamStatus status)
	{
		for (IForgePlayer player : FTBLibAPI.API.getUniverse().getPlayers())
		{
			if (hasStatus(player, status))
			{
				collection.add(player);
			}
		}

		return collection;
	}

	default List<IForgePlayer> getPlayersWithStatus(EnumTeamStatus status)
	{
		List<IForgePlayer> list = new ArrayList<>();
		getPlayersWithStatus(list, status);
		return list;
	}

	boolean addMember(IForgePlayer player);

	boolean removeMember(IForgePlayer player);

	default List<IForgePlayer> getMembers()
	{
		return getPlayersWithStatus(EnumTeamStatus.MEMBER);
	}

	default boolean isMember(@Nullable IForgePlayer player)
	{
		return player != null && equalsTeam(player.getTeam());
	}

	boolean isAlly(@Nullable IForgePlayer player);

	boolean isInvited(@Nullable IForgePlayer player);

	boolean isRequestingInvite(@Nullable IForgePlayer player);

	boolean isEnemy(@Nullable IForgePlayer player);

	boolean isModerator(@Nullable IForgePlayer player);

	default boolean isOwner(@Nullable IForgePlayer player)
	{
		return getOwner().equalsPlayer(player);
	}

	ConfigGroup getSettings();

	default boolean anyPlayerHasPermission(String permission, EnumTeamStatus status)
	{
		for (IForgePlayer player : getPlayersWithStatus(status))
		{
			if (PermissionAPI.hasPermission(player.getProfile(), permission, null))
			{
				return true;
			}
		}

		return false;
	}
}