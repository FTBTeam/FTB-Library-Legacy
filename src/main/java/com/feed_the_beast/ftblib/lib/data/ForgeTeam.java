package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLibFinals;
import com.feed_the_beast.ftblib.FTBLibMod;
import com.feed_the_beast.ftblib.FTBLibModCommon;
import com.feed_the_beast.ftblib.events.team.ForgeTeamConfigEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamOwnerChangedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftblib.lib.EnumTeamColor;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.config.ConfigBoolean;
import com.feed_the_beast.ftblib.lib.config.ConfigEnum;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigString;
import com.feed_the_beast.ftblib.lib.gui.GuiLang;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.FileUtils;
import com.feed_the_beast.ftblib.lib.util.FinalIDObject;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.permission.PermissionAPI;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author LatvianModder
 */
public final class ForgeTeam extends FinalIDObject implements IStringSerializable
{
	public boolean isValid;
	public final NBTDataStorage dataStorage;
	public final ConfigEnum<EnumTeamColor> color;
	public final ConfigEnum<EnumTeamStatus> fakePlayerStatus;
	public ForgePlayer owner;
	public final ConfigString title;
	public final ConfigString desc;
	public final ConfigBoolean freeToJoin;
	public final Collection<ForgePlayer> requestingInvite;
	public final Map<ForgePlayer, EnumTeamStatus> players;
	private ConfigGroup cachedConfig;

	public ForgeTeam(String id)
	{
		super(id);
		isValid = true;
		color = new ConfigEnum<>(EnumTeamColor.NAME_MAP);
		fakePlayerStatus = new ConfigEnum<>(EnumTeamStatus.NAME_MAP_PERMS);
		title = new ConfigString("");
		desc = new ConfigString("");
		freeToJoin = new ConfigBoolean(false);
		requestingInvite = new HashSet<>();
		players = new HashMap<>();

		dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_TEAM);
	}

	public NBTDataStorage getData()
	{
		return dataStorage;
	}

	public ForgePlayer getOwner()
	{
		return owner;
	}

	public String getTitle()
	{
		return title.isEmpty() ? (owner.getName() + (owner.getName().endsWith("s") ? "' Team" : "'s Team")) : title.getString();
	}

	public String getDesc()
	{
		return desc.getString();
	}

	public EnumTeamColor getColor()
	{
		return color.getValue();
	}

	public EnumTeamStatus getFakePlayerStatus()
	{
		return fakePlayerStatus.getValue();
	}

	public EnumTeamStatus getHighestStatus(@Nullable ForgePlayer player)
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

	public void setColor(EnumTeamColor col)
	{
		color.setValue(col);
	}

	private EnumTeamStatus getSetStatus(@Nullable ForgePlayer player)
	{
		if (player == null)
		{
			return EnumTeamStatus.NONE;
		}

		EnumTeamStatus status = players.get(player);
		return status == null ? EnumTeamStatus.NONE : status;
	}

	public boolean hasStatus(@Nullable ForgePlayer player, EnumTeamStatus status)
	{
		if (player == null)
		{
			return false;
		}

		if (player.isFake())
		{
			return getFakePlayerStatus().isEqualOrGreaterThan(status);
		}

		switch (status)
		{
			case NONE:
				return true;
			case ENEMY:
				return isEnemy(player);
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

	public boolean setStatus(@Nullable ForgePlayer player, EnumTeamStatus status)
	{
		if (player == null)
		{
			return false;
		}
		else if (status == EnumTeamStatus.OWNER)
		{
			if (!isMember(player))
			{
				return false;
			}

			if (!player.equalsPlayer(owner))
			{
				ForgePlayer oldOwner = owner;
				owner = player;
				players.remove(player);
				new ForgeTeamOwnerChangedEvent(this, oldOwner).post();
				return true;
			}

			return false;
		}
		else if (!status.isNone() && status.canBeSet())
		{
			return players.put(player, status) != status;
		}
		else
		{
			return players.remove(player) != status;
		}
	}

	public Collection<ForgePlayer> getPlayersWithStatus(Collection<ForgePlayer> collection, EnumTeamStatus status)
	{
		for (ForgePlayer player : Universe.get().getPlayers())
		{
			if (!player.isFake() && hasStatus(player, status))
			{
				collection.add(player);
			}
		}

		return collection;
	}

	public List<ForgePlayer> getPlayersWithStatus(EnumTeamStatus status)
	{
		List<ForgePlayer> list = new ArrayList<>();
		getPlayersWithStatus(list, status);
		return list;
	}

	public boolean addMember(ForgePlayer player)
	{
		if ((isOwner(player) || isInvited(player)) && !isMember(player))
		{
			player.setTeamId(getName());
			players.remove(player);
			requestingInvite.remove(player);
			new ForgeTeamPlayerJoinedEvent(this, player).post();
			return true;
		}

		return false;
	}

	public boolean removeMember(ForgePlayer player)
	{
		if (!isMember(player))
		{
			return false;
		}
		else if (getMembers().size() == 1)
		{
			new ForgeTeamDeletedEvent(this).post();
			removePlayer0(player);
			Universe.get().teams.remove(getName());
			FileUtils.delete(new File(CommonUtils.folderWorld, "data/ftb_lib/teams/" + getName() + ".dat"));
		}
		else if (isOwner(player))
		{
			return false;
		}

		removePlayer0(player);
		return true;
	}

	private void removePlayer0(ForgePlayer player)
	{
		player.setTeamId("");
		setStatus(player, EnumTeamStatus.NONE);
		new ForgeTeamPlayerLeftEvent(this, player).post();
	}

	public List<ForgePlayer> getMembers()
	{
		return getPlayersWithStatus(EnumTeamStatus.MEMBER);
	}

	public boolean isMember(@Nullable ForgePlayer player)
	{
		return player != null && equalsTeam(player.getTeam());
	}

	public boolean isAlly(@Nullable ForgePlayer player)
	{
		return isMember(player) || getSetStatus(player).isEqualOrGreaterThan(EnumTeamStatus.ALLY);
	}

	public boolean isInvited(@Nullable ForgePlayer player)
	{
		return isMember(player) || ((freeToJoin.getBoolean() || getSetStatus(player).isEqualOrGreaterThan(EnumTeamStatus.INVITED)) && !isEnemy(player));
	}

	public boolean setRequestingInvite(@Nullable ForgePlayer player, boolean value)
	{
		if (player != null)
		{
			if (value)
			{
				return requestingInvite.add(player);
			}
			else
			{
				return requestingInvite.remove(player);
			}
		}

		return false;
	}

	public boolean isRequestingInvite(@Nullable ForgePlayer player)
	{
		return player != null && !isMember(player) && requestingInvite.contains(player) && !isEnemy(player);
	}

	public boolean isEnemy(@Nullable ForgePlayer player)
	{
		return getSetStatus(player) == EnumTeamStatus.ENEMY;
	}

	public boolean isModerator(@Nullable ForgePlayer player)
	{
		return isOwner(player) || isMember(player) && getSetStatus(player).isEqualOrGreaterThan(EnumTeamStatus.MOD);
	}

	public boolean isOwner(@Nullable ForgePlayer player)
	{
		return player != null && player.equalsPlayer(getOwner());
	}

	public ConfigGroup getSettings()
	{
		if (cachedConfig == null)
		{
			cachedConfig = new ConfigGroup(GuiLang.SETTINGS.textComponent(null));
			cachedConfig.setSupergroup("team_config");
			ForgeTeamConfigEvent event = new ForgeTeamConfigEvent(this, cachedConfig);
			event.post();

			String group = FTBLibFinals.MOD_ID;
			event.getConfig().setGroupName(group, new TextComponentString(FTBLibFinals.MOD_NAME));
			event.getConfig().add(group, "free_to_join", freeToJoin);
			group = FTBLibFinals.MOD_ID + ".display";
			event.getConfig().add(group, "color", color);
			event.getConfig().add(group, "fake_player_status", fakePlayerStatus);
			event.getConfig().add(group, "title", title);
			event.getConfig().add(group, "desc", desc);
		}

		return cachedConfig;
	}

	public boolean isValid()
	{
		return isValid;
	}

	public boolean equalsTeam(@Nullable ForgeTeam team)
	{
		return team == this || (team != null && getName().equals(team.getName()));
	}

	public boolean anyPlayerHasPermission(String permission, EnumTeamStatus status)
	{
		for (ForgePlayer player : getPlayersWithStatus(status))
		{
			if (PermissionAPI.hasPermission(player.getProfile(), permission, null))
			{
				return true;
			}
		}

		return false;
	}
}