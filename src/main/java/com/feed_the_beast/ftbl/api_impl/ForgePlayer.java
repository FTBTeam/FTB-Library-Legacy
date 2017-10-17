package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IForgeTeam;
import com.feed_the_beast.ftbl.api.player.ForgePlayerConfigEvent;
import com.feed_the_beast.ftbl.api.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.NBTUtils;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import com.feed_the_beast.ftbl.lib.util.misc.NBTDataStorage;
import com.feed_the_beast.ftbl.net.MessageSyncData;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class ForgePlayer implements IForgePlayer, Comparable<ForgePlayer>
{
	private static FakePlayer playerForStats;

	private final UUID playerId;
	private String playerName;
	public boolean firstLogin;
	public final NBTDataStorage dataStorage;
	public IForgeTeam team = null;
	public final ConfigBoolean hideTeamNotification;
	public EntityPlayerMP entityPlayer;
	public NBTTagCompound playerNBT;
	public final ConfigGroup cachedConfig;
	public boolean loggingOut;
	public long lastTimeSeen;

	public ForgePlayer(UUID id, String name)
	{
		playerId = id;
		playerName = name;
		firstLogin = true;
		dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_PLAYER);
		hideTeamNotification = new ConfigBoolean();

		cachedConfig = new ConfigGroup(FTBLibLang.MY_SERVER_SETTINGS.textComponent());
		cachedConfig.setSupergroup("player_config");
		ForgePlayerConfigEvent event = new ForgePlayerConfigEvent(this, cachedConfig);
		event.post();
		String group = FTBLibFinals.MOD_ID;
		event.getConfig().setGroupName(group, new TextComponentString(FTBLibFinals.MOD_NAME));
		event.getConfig().add(group, "hide_team_notification", hideTeamNotification);
	}

	@Override
	public final void setTeamId(String id)
	{
		team = FTBLibAPI.API.getUniverse().getTeam(id);
	}

	@Override
	@Nullable
	public final IForgeTeam getTeam()
	{
		if (team != null && !team.isValid())
		{
			team = null;
		}

		return team;
	}

	@Override
	public final GameProfile getProfile()
	{
		if (isOnline())
		{
			return entityPlayer.getGameProfile();
		}

		return new GameProfile(playerId, playerName);
	}

	@Override
	public final UUID getId()
	{
		return playerId;
	}

	@Override
	public final String getName()
	{
		return playerName;
	}

	public final void setUsername(String n)
	{
		playerName = n;
	}

	@Override
	public NBTDataStorage getData()
	{
		return dataStorage;
	}

	@Override
	public final int compareTo(ForgePlayer o)
	{
		return getName().compareToIgnoreCase(o.getName());
	}

	public final String toString()
	{
		return playerName;
	}

	public final int hashCode()
	{
		return playerId.hashCode();
	}

	public boolean equals(Object o)
	{
		if (o == null)
		{
			return false;
		}
		else if (o == this || o == playerId)
		{
			return true;
		}
		else if (o instanceof IForgePlayer)
		{
			return equalsPlayer((IForgePlayer) o);
		}
		return (o.getClass() == UUID.class) && playerId.equals(o);
	}

	@Override
	public boolean isOnline()
	{
		return entityPlayer != null;
	}

	@Override
	public EntityPlayerMP getPlayer()
	{
		Objects.requireNonNull(entityPlayer, "EntityPlayer can't be null!");
		return entityPlayer;
	}

	@Override
	public boolean isFake()
	{
		return entityPlayer instanceof FakePlayer;
	}

	@Override
	public boolean isOP()
	{
		return ServerUtils.isOP(getProfile());
	}

	public void onLoggedIn(EntityPlayerMP ep, boolean firstLogin)
	{
		entityPlayer = ep;
		playerNBT = null;
		loggingOut = false;

		if (!isFake())
		{
			//FTBLibStats.updateLastSeen(stats());
			new MessageSyncData(ep, this).sendTo(entityPlayer);
		}

		new ForgePlayerLoggedInEvent(this, firstLogin).post();
	}

	@Override
	public StatisticsManagerServer stats()
	{
		if (playerForStats == null)
		{
			playerForStats = new FakePlayer(ServerUtils.getOverworld(), new GameProfile(new UUID(0L, 0L), "_unknown"));
		}

		playerForStats.setUniqueId(getId());
		return ServerUtils.getServer().getPlayerList().getPlayerStatsFile(playerForStats);
	}

	@Override
	public ConfigGroup getSettings()
	{
		return cachedConfig;
	}

	@Override
	public NBTTagCompound getPlayerNBT()
	{
		if (isOnline())
		{
			return getPlayer().serializeNBT();
		}

		if (playerNBT == null)
		{
			try
			{
				playerNBT = NBTUtils.readTag(new File(CommonUtils.folderWorld, "playerdata/" + getId() + ".dat"));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return playerNBT;
	}

	@Override
	public void setPlayerNBT(NBTTagCompound nbt)
	{
		//FIXME
	}

	@Override
	public boolean hideTeamNotification()
	{
		return hideTeamNotification.getBoolean() || isFake();
	}

	public void setLoggingOut(boolean v)
	{
		loggingOut = v;
	}

	@Override
	public boolean isLoggingOut()
	{
		return loggingOut;
	}

	@Override
	public long getLastTimeSeen()
	{
		return isOnline() ? CommonUtils.getWorldTime() : lastTimeSeen;
	}
}