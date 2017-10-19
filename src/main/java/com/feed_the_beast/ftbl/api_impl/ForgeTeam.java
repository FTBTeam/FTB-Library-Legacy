package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.team.ForgeTeamConfigEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamOwnerChangedEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigEnum;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigString;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.FileUtils;
import com.feed_the_beast.ftbl.lib.util.FinalIDObject;
import com.feed_the_beast.ftbl.lib.util.misc.NBTDataStorage;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author LatvianModder
 */
public final class ForgeTeam extends FinalIDObject implements IForgeTeam
{
	public boolean isValid;
	public final NBTDataStorage dataStorage;
	public final ConfigEnum<EnumTeamColor> color;
	public IForgePlayer owner;
	public final ConfigString title;
	public final ConfigString desc;
	public final ConfigBoolean freeToJoin;
	public final Collection<IForgePlayer> requestingInvite;
	public final Map<IForgePlayer, EnumTeamStatus> players;
	public final ConfigGroup cachedConfig;

	public ForgeTeam(String id)
	{
		super(id);
		isValid = true;
		color = new ConfigEnum<>(EnumTeamColor.NAME_MAP);
		title = new ConfigString("");
		desc = new ConfigString("");
		freeToJoin = new ConfigBoolean(false);
		requestingInvite = new HashSet<>();
		players = new HashMap<>();

		dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_TEAM);

		cachedConfig = new ConfigGroup(FTBLibLang.MY_TEAM_SETTINGS.textComponent());
		cachedConfig.setSupergroup("team_config");
		ForgeTeamConfigEvent event = new ForgeTeamConfigEvent(this, cachedConfig);
		event.post();

		String group = FTBLibFinals.MOD_ID;
		event.getConfig().setGroupName(group, new TextComponentString(FTBLibFinals.MOD_NAME));
		event.getConfig().add(group, "free_to_join", freeToJoin);
		group = FTBLibFinals.MOD_ID + ".display";
		event.getConfig().add(group, "color", color);
		event.getConfig().add(group, "title", title);
		event.getConfig().add(group, "desc", desc);
	}

	@Override
	public NBTDataStorage getData()
	{
		return dataStorage;
	}

	@Override
	public IForgePlayer getOwner()
	{
		return owner;
	}

	@Override
	public String getTitle()
	{
		return title.isEmpty() ? (owner.getName() + (owner.getName().endsWith("s") ? "' Team" : "'s Team")) : title.getString();
	}

	@Override
	public String getDesc()
	{
		return desc.getString();
	}

	@Override
	public EnumTeamColor getColor()
	{
		return color.getValue();
	}

	public void setColor(EnumTeamColor col)
	{
		color.setValue(col);
	}

	private EnumTeamStatus getSetStatus(@Nullable IForgePlayer player)
	{
		if (player == null)
		{
			return EnumTeamStatus.NONE;
		}

		EnumTeamStatus status = players.get(player);
		return status == null ? EnumTeamStatus.NONE : status;
	}

	@Override
	public boolean setStatus(@Nullable IForgePlayer player, EnumTeamStatus status)
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
				IForgePlayer oldOwner = owner;
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

	@Override
	public boolean addMember(IForgePlayer player)
	{
		if (isInvited(player) && !isMember(player))
		{
			player.setTeamId(getName());
			players.remove(player);
			requestingInvite.remove(player);
			new ForgeTeamPlayerJoinedEvent(this, player).post();
			return true;
		}

		return false;
	}

	@Override
	public boolean removeMember(IForgePlayer player)
	{
		if (!isMember(player))
		{
			return false;
		}
		else if (getMembers().size() == 1)
		{
			new ForgeTeamDeletedEvent(this).post();
			removePlayer0(player);
			Universe.INSTANCE.teams.remove(getName());
			FileUtils.delete(new File(CommonUtils.folderWorld, "data/ftb_lib/teams/" + getName() + ".dat"));
		}
		else if (isOwner(player))
		{
			return false;
		}

		removePlayer0(player);
		return true;
	}

	private void removePlayer0(IForgePlayer player)
	{
		player.setTeamId("");
		setStatus(player, EnumTeamStatus.NONE);
		new ForgeTeamPlayerLeftEvent(this, player).post();
	}

	@Override
	public boolean isAlly(@Nullable IForgePlayer player)
	{
		return isMember(player) || getSetStatus(player).isEqualOrGreaterThan(EnumTeamStatus.ALLY);
	}

	@Override
	public boolean isInvited(@Nullable IForgePlayer player)
	{
		return isMember(player) || ((freeToJoin.getBoolean() || getSetStatus(player).isEqualOrGreaterThan(EnumTeamStatus.INVITED)) && !isEnemy(player));
	}

	@Override
	public boolean setRequestingInvite(@Nullable IForgePlayer player, boolean value)
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

	@Override
	public boolean isRequestingInvite(@Nullable IForgePlayer player)
	{
		return player != null && !isMember(player) && requestingInvite.contains(player) && !isEnemy(player);
	}

	@Override
	public boolean isEnemy(@Nullable IForgePlayer player)
	{
		return getSetStatus(player) == EnumTeamStatus.ENEMY;
	}

	@Override
	public boolean isModerator(@Nullable IForgePlayer player)
	{
		return isOwner(player) || isMember(player) && getSetStatus(player).isEqualOrGreaterThan(EnumTeamStatus.MOD);
	}

	@Override
	public ConfigGroup getSettings()
	{
		return cachedConfig;
	}

	@Override
	public boolean isValid()
	{
		return isValid;
	}
}