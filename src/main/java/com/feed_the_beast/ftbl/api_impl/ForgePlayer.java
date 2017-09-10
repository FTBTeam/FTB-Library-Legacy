package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibMod;
import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerConfigEvent;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerDeathEvent;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerLoggedOutEvent;
import com.feed_the_beast.ftbl.lib.NBTDataStorage;
import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.NBTUtils;
import com.feed_the_beast.ftbl.lib.util.ServerUtils;
import com.feed_the_beast.ftbl.net.MessageLogin;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.io.File;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class ForgePlayer implements IForgePlayer, Comparable<ForgePlayer>
{
	private static FakePlayer playerForStats;

	private final UUID playerId;
	private String playerName;
	private final NBTDataStorage dataStorage;
	private ForgeTeam team = null;
	private final ConfigBoolean hideTeamNotification;
	private EntityPlayerMP entityPlayer;
	private NBTTagCompound playerNBT;
	private final ConfigGroup cachedConfig;
	private boolean loggingOut;

	public ForgePlayer(UUID id, String name)
	{
		playerId = id;
		playerName = name;
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
	public final void setTeamID(String id)
	{
		team = Universe.INSTANCE.getTeam(id);
	}

	@Override
	@Nullable
	public final ForgeTeam getTeam()
	{
		if (team != null && !team.isValid())
		{
			return null;
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
	@Nullable
	public INBTSerializable<?> getData(ResourceLocation id)
	{
		return dataStorage == null ? null : dataStorage.get(id);
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
		else if (o instanceof UUID)
		{
			return playerId.equals(o);
		}
		else if (o instanceof IForgePlayer)
		{
			return equalsPlayer((IForgePlayer) o);
		}
		return equalsPlayer(Universe.INSTANCE.getPlayer(o));
	}

	@Override
	public boolean equalsPlayer(@Nullable IForgePlayer p)
	{
		return p == this || (p != null && getId().equals(p.getId()));
	}

	@Override
	public boolean isOnline()
	{
		return entityPlayer != null;
	}

	@Override
	public EntityPlayerMP getPlayer()
	{
		Preconditions.checkNotNull(entityPlayer, "EntityPlayer can't be null!");
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

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		hideTeamNotification.setBoolean(nbt.getBoolean("HideTeamNotification"));

		setTeamID(nbt.getString("TeamID"));

		if (dataStorage != null)
		{
			dataStorage.deserializeNBT(nbt.hasKey("Caps") ? nbt.getCompoundTag("Caps") : nbt.getCompoundTag("Data"));
		}
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();

		nbt.setBoolean("HideTeamNotification", hideTeamNotification.getBoolean());

		if (team != null && team.isValid())
		{
			nbt.setString("TeamID", team.getName());
		}

		if (dataStorage != null)
		{
			nbt.setTag("Data", dataStorage.serializeNBT());
		}

		return nbt;
	}

	public void onLoggedIn(EntityPlayerMP ep, boolean firstLogin)
	{
		entityPlayer = ep;
		playerNBT = null;
		loggingOut = false;

		if (!isFake())
		{
			//FTBLibStats.updateLastSeen(stats());
			new MessageLogin(ep, this).sendTo(entityPlayer);
		}

		new ForgePlayerLoggedInEvent(this, firstLogin).post();
	}

	public void onLoggedOut()
	{
		loggingOut = true;
		//FTBLibStats.updateLastSeen(stats());
		new ForgePlayerLoggedOutEvent(this).post();
		entityPlayer = null;
		playerNBT = null;
	}

	public void onDeath(EntityPlayerMP ep, DamageSource ds)
	{
		entityPlayer = ep;

		if (isOnline())
		{
			//FTBLibStats.updateLastSeen(stats());
			new ForgePlayerDeathEvent(this, ds).post();
		}
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
}