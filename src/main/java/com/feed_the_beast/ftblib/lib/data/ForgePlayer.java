package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLibFinals;
import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.FTBLibMod;
import com.feed_the_beast.ftblib.FTBLibModCommon;
import com.feed_the_beast.ftblib.events.player.ForgePlayerConfigEvent;
import com.feed_the_beast.ftblib.events.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftblib.events.player.ForgePlayerLoggedOutEvent;
import com.feed_the_beast.ftblib.lib.config.ConfigBoolean;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.NBTUtils;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.lib.util.misc.EnumPrivacyLevel;
import com.feed_the_beast.ftblib.net.MessageSyncData;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.io.File;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class ForgePlayer implements IStringSerializable, Comparable<ForgePlayer>
{
	private static FakePlayer playerForStats;

	private final UUID playerId;
	private String playerName;
	public boolean firstLogin;
	public final NBTDataStorage dataStorage;
	public ForgeTeam team = null;
	public final ConfigBoolean hideTeamNotification;
	public EntityPlayerMP entityPlayer;
	public NBTTagCompound playerNBT;
	private ConfigGroup cachedConfig;
	public long lastTimeSeen;

	public ForgePlayer(UUID id, String name)
	{
		playerId = id;
		playerName = name;
		firstLogin = true;
		dataStorage = FTBLibMod.PROXY.createDataStorage(this, FTBLibModCommon.DATA_PROVIDER_PLAYER);
		hideTeamNotification = new ConfigBoolean();
	}

	public final void setTeamId(String id)
	{
		team = Universe.get().getTeam(id);
	}

	@Nullable
	public final ForgeTeam getTeam()
	{
		if (team != null && !team.isValid())
		{
			team = null;
		}

		return team;
	}

	public final GameProfile getProfile()
	{
		if (isOnline())
		{
			return entityPlayer.getGameProfile();
		}

		return new GameProfile(playerId, playerName);
	}

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
		if (!playerName.equals(n))
		{
			new File(CommonUtils.folderWorld, "data/ftb_lib/players/" + playerName.toLowerCase() + ".dat").delete();
			playerName = n;
		}
	}

	public EntityPlayerMP getCommandPlayer() throws CommandException
	{
		if (!isOnline())
		{
			throw FTBLibLang.PLAYER_NOT_FOUND.commandError(getName());
		}

		return getPlayer();
	}

	public NBTDataStorage getData()
	{
		return dataStorage;
	}

	public boolean equalsPlayer(@Nullable ForgePlayer player)
	{
		return player == this || (player != null && getId().equals(player.getId()));
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
		return o == this || o instanceof ForgePlayer && equalsPlayer((ForgePlayer) o);
	}

	public boolean canInteract(@Nullable ForgePlayer owner, EnumPrivacyLevel level)
	{
		if (level == EnumPrivacyLevel.PUBLIC || owner == null)
		{
			return true;
		}
		else if (owner.equalsPlayer(this))
		{
			return true;
		}
		else if (level == EnumPrivacyLevel.PRIVATE)
		{
			return false;
		}
		else if (level == EnumPrivacyLevel.TEAM)
		{
			ForgeTeam team = owner.getTeam();

			if (team != null && team.isAlly(this))
			{
				return true;
			}
		}

		return false;
	}

	public boolean isOnline()
	{
		return entityPlayer != null;
	}

	public EntityPlayerMP getPlayer()
	{
		Objects.requireNonNull(entityPlayer, "EntityPlayer can't be null!");
		return entityPlayer;
	}

	public boolean isFake()
	{
		return entityPlayer instanceof FakePlayer;
	}

	public boolean isOP()
	{
		return ServerUtils.isOP(getProfile());
	}

	public void onLoggedIn(EntityPlayerMP player, boolean firstLogin)
	{
		entityPlayer = player;
		playerNBT = null;

		if (!isFake())
		{
			//FTBLibStats.updateLastSeen(stats());
			new MessageSyncData(player, this).sendTo(entityPlayer);
		}

		new ForgePlayerLoggedInEvent(this, firstLogin).post();
	}

	public void onLoggedOut()
	{
		if (entityPlayer != null)
		{
			lastTimeSeen = entityPlayer.world.getTotalWorldTime();
			//FTBLibStats.updateLastSeen(stats());
			new ForgePlayerLoggedOutEvent(this).post();
			entityPlayer = null;
			playerNBT = null;
		}
	}

	public StatisticsManagerServer stats()
	{
		if (playerForStats == null)
		{
			playerForStats = new FakePlayer(ServerUtils.getOverworld(), new GameProfile(new UUID(0L, 0L), "_unknown"));
		}

		playerForStats.setUniqueId(getId());
		return ServerUtils.getServer().getPlayerList().getPlayerStatsFile(playerForStats);
	}

	public ConfigGroup getSettings()
	{
		if (cachedConfig == null)
		{
			cachedConfig = new ConfigGroup(FTBLibLang.MY_SERVER_SETTINGS.textComponent(null));
			cachedConfig.setSupergroup("player_config");
			ForgePlayerConfigEvent event = new ForgePlayerConfigEvent(this, cachedConfig);
			event.post();
			String group = FTBLibFinals.MOD_ID;
			event.getConfig().setGroupName(group, new TextComponentString(FTBLibFinals.MOD_NAME));
			event.getConfig().add(group, "hide_team_notification", hideTeamNotification);
		}

		return cachedConfig;
	}

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

	public void setPlayerNBT(NBTTagCompound nbt)
	{
		//FIXME
	}

	public boolean hideTeamNotification()
	{
		return hideTeamNotification.getBoolean() || isFake();
	}

	public long getLastTimeSeen()
	{
		return isOnline() ? CommonUtils.getWorldTime() : lastTimeSeen;
	}
}