package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.EnumTeamColor;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.team.ForgeTeamConfigEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamDeletedEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamOwnerChangedEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamPlayerLeftEvent;
import com.feed_the_beast.ftbl.lib.FinalIDObject;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigEnum;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.ConfigString;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.FileUtils;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
	public final Map<UUID, EnumTeamStatus> players;
	public final ConfigGroup cachedConfig;

	public ForgeTeam(String id, @Nullable IForgePlayer _owner)
	{
		super(id);
		isValid = true;
		color = new ConfigEnum<>(EnumTeamColor.NAME_MAP);
		owner = _owner;
		title = new ConfigString("");
		desc = new ConfigString("");
		freeToJoin = new ConfigBoolean(false);
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

	@Override
	public EnumTeamStatus getHighestStatus(UUID playerId)
	{
		if (owner.getId().equals(playerId))
		{
			return EnumTeamStatus.OWNER;
		}

		EnumTeamStatus status = getSetStatus(playerId);

		if (status == EnumTeamStatus.MOD)
		{
			if (!isMember(playerId))
			{
				status = EnumTeamStatus.NONE;
			}
		}
		else if (!status.isEqualOrGreaterThan(EnumTeamStatus.MEMBER) && isMember(playerId))
		{
			status = EnumTeamStatus.MEMBER;
		}

		return status;
	}

	private boolean isMember(UUID playerId)
	{
		IForgePlayer player = FTBLibAPI.API.getUniverse().getPlayer(playerId);
		return player != null && equals(player.getTeam());

	}

	private EnumTeamStatus getSetStatus(UUID playerId)
	{
		if (players == null || players.isEmpty())
		{
			return EnumTeamStatus.NONE;
		}

		EnumTeamStatus status = players.get(playerId);

		if (status == null)
		{
			status = EnumTeamStatus.NONE;

			if (freeToJoin.getBoolean())
			{
				status = EnumTeamStatus.INVITED;
			}
		}

		return status;
	}

	@Override
	public boolean hasStatus(UUID playerId, EnumTeamStatus status)
	{
		if (status.isNone())
		{
			return true;
		}

		EnumTeamStatus status1 = getHighestStatus(playerId);
		return status1.isEqualOrGreaterThan(status);
	}

	@Override
	public void setStatus(UUID playerId, EnumTeamStatus status)
	{
		if (!status.isNone())
		{
			players.put(playerId, status);
		}
		else
		{
			players.remove(playerId);
		}
	}

	@Override
	public Collection<IForgePlayer> getPlayersWithStatus(Collection<IForgePlayer> c, EnumTeamStatus status)
	{
		for (IForgePlayer p : Universe.INSTANCE.getPlayers())
		{
			if (hasStatus(p, status))
			{
				c.add(p);
			}
		}

		return c;
	}

	@Override
	public boolean addPlayer(IForgePlayer player)
	{
		if (hasStatus(player, EnumTeamStatus.INVITED))
		{
			player.setTeamId(getName());

			if (!hasStatus(player, EnumTeamStatus.MEMBER))
			{
				setStatus(player.getId(), EnumTeamStatus.MEMBER);
				new ForgeTeamPlayerJoinedEvent(this, player).post();
			}

			return true;
		}

		return false;
	}

	@Override
	public boolean removePlayer(IForgePlayer player)
	{
		if (getPlayersWithStatus(new ArrayList<>(), EnumTeamStatus.MEMBER).size() == 1)
		{
			new ForgeTeamDeletedEvent(this).post();
			removePlayer0(player);
			Universe.INSTANCE.teams.remove(getName());
			FileUtils.delete(new File(CommonUtils.folderWorld, "data/ftb_lib/teams/" + getName() + ".dat"));
		}
		else
		{
			if (hasStatus(player, EnumTeamStatus.OWNER))
			{
				return false;
			}

			removePlayer0(player);
		}

		return true;
	}

	private void removePlayer0(IForgePlayer player)
	{
		if (hasStatus(player, EnumTeamStatus.MEMBER))
		{
			player.setTeamId("");
			new ForgeTeamPlayerLeftEvent(this, player).post();
		}
	}

	@Override
	public void changeOwner(IForgePlayer player)
	{
		if (!hasStatus(player, EnumTeamStatus.MEMBER))
		{
			return;
		}

		IForgePlayer oldOwner = owner;
		owner = player;
		player.setTeamId(getName());

		if (!oldOwner.equalsPlayer(owner))
		{
			new ForgeTeamOwnerChangedEvent(this, oldOwner, player).post();
		}
	}

	@Override
	public ConfigGroup getSettings()
	{
		return cachedConfig;
	}

	public Collection<EntityPlayerMP> getOnlineTeamPlayers(EnumTeamStatus status)
	{
		Collection<EntityPlayerMP> list = new ArrayList<>();

		for (EntityPlayerMP ep : ServerUtils.getServer().getPlayerList().getPlayers())
		{
			if (hasStatus(ep.getGameProfile().getId(), status))
			{
				list.add(ep);
			}
		}

		return list;
	}

	@Override
	public boolean isValid()
	{
		return isValid;
	}

	@Override
	public boolean freeToJoin()
	{
		return freeToJoin.getBoolean();
	}
}