package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.events.player.ForgePlayerConfigEvent;
import com.feed_the_beast.ftblib.events.player.ForgePlayerDataEvent;
import com.feed_the_beast.ftblib.events.player.ForgePlayerLoggedOutEvent;
import com.feed_the_beast.ftblib.lib.config.ConfigBoolean;
import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.ConfigValue;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.config.RankConfigAPI;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.FileUtils;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.lib.util.misc.EnumPrivacyLevel;
import com.feed_the_beast.ftblib.lib.util.misc.Node;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.server.permission.PermissionAPI;
import net.minecraftforge.server.permission.context.IContext;

import javax.annotation.Nullable;
import java.io.File;
import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class ForgePlayer implements IStringSerializable, INBTSerializable<NBTTagCompound>, Comparable<ForgePlayer>, IHasCache
{
	private static FakePlayer playerForStats;

	private final UUID playerId;
	private String playerName;
	public final HashSet<ResourceLocation> firstLogin;
	public final NBTDataStorage dataStorage;
	public ForgeTeam team;
	private final ConfigBoolean hideTeamNotification;
	public EntityPlayerMP entityPlayer;
	public NBTTagCompound cachedPlayerNBT;
	private ConfigGroup cachedConfig;
	private GameProfile cachedProfile;
	public long lastTimeSeen;
	private IConfigCallback cachedConfigCallback;
	public boolean needsSaving;

	public ForgePlayer(Universe u, UUID id, String name)
	{
		playerId = id;
		playerName = name;
		firstLogin = new HashSet<>();
		dataStorage = new NBTDataStorage();
		team = u.getTeam("");
		hideTeamNotification = new ConfigBoolean();
		new ForgePlayerDataEvent(this, dataStorage::add).post();
		clearCache();
		needsSaving = false;
	}

	@Override
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setBoolean("HideTeamNotification", hideTeamNotification.getBoolean());
		nbt.setLong("LastTimeSeen", lastTimeSeen);
		nbt.setTag("Data", dataStorage.serializeNBT());

		NBTTagList list = new NBTTagList();

		for (ResourceLocation id : firstLogin)
		{
			list.appendTag(new NBTTagString(id.toString()));
		}

		nbt.setTag("FirstLogin", list);
		return nbt;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt)
	{
		hideTeamNotification.setBoolean(nbt.getBoolean("HideTeamNotification"));
		lastTimeSeen = nbt.getLong("LastTimeSeen");
		dataStorage.deserializeNBT(nbt.getCompoundTag("Data"));

		NBTTagList list = nbt.getTagList("FirstLogin", Constants.NBT.TAG_STRING);

		for (int i = 0; i < list.tagCount(); i++)
		{
			firstLogin.add(new ResourceLocation(list.getStringTagAt(i)));
		}
	}

	@Override
	public void clearCache()
	{
		cachedPlayerNBT = null;
		cachedProfile = null;
		cachedConfig = null;
		cachedConfigCallback = null;
		dataStorage.clearCache();
	}

	public void markDirty()
	{
		needsSaving = true;
		team.universe.checkSaving = true;
	}

	public boolean hasTeam()
	{
		return team.isValid();
	}

	public final GameProfile getProfile()
	{
		if (isFake())
		{
			return CommonUtils.FAKE_PLAYER_PROFILE;
		}
		else if (isOnline())
		{
			return entityPlayer.getGameProfile();
		}

		if (cachedProfile == null)
		{
			cachedProfile = new GameProfile(playerId, playerName);
		}

		return cachedProfile;
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

	public final void setName(String n)
	{
		if (!isFake() && !playerName.equals(n))
		{
			new File(team.universe.world.getSaveHandler().getWorldDirectory(), "data/ftb_lib/players/" + playerName.toLowerCase() + ".dat").delete();
			playerName = n;
			markDirty();
		}
	}

	public final String getDisplayName()
	{
		return isOnline() ? getPlayer().getDisplayNameString() : getName();
	}

	public EntityPlayerMP getCommandPlayer() throws CommandException
	{
		if (!isOnline())
		{
			throw new CommandException("commands.generic.player.notFound", getDisplayName());
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

	public boolean equalsPlayer(@Nullable ICommandSender player)
	{
		return player == entityPlayer || (player instanceof EntityPlayerMP && ((EntityPlayerMP) player).getUniqueID().equals(getId()));
	}

	@Override
	public final int compareTo(ForgePlayer o)
	{
		return StringUtils.IGNORE_CASE_COMPARATOR.compare(getDisplayName(), o.getDisplayName());
	}

	public final String toString()
	{
		return getDisplayName();
	}

	public final int hashCode()
	{
		return getId().hashCode();
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
			return owner.team.isAlly(this);
		}

		return false;
	}

	public boolean isOnline()
	{
		return entityPlayer != null;
	}

	public EntityPlayerMP getPlayer()
	{
		return Objects.requireNonNull(entityPlayer, "EntityPlayer can't be null!");
	}

	public boolean isFake()
	{
		return false;
	}

	public boolean isOP()
	{
		return ServerUtils.isOP(team.universe.server, getProfile());
	}

	public void onLoggedOut()
	{
		if (entityPlayer != null)
		{
			lastTimeSeen = entityPlayer.world.getTotalWorldTime();
			//FTBLibStats.updateLastSeen(stats());
			new ForgePlayerLoggedOutEvent(this).post();
			entityPlayer = null;
			clearCache();
			markDirty();
		}
	}

	public StatisticsManagerServer stats()
	{
		if (playerForStats == null)
		{
			playerForStats = new FakePlayer(team.universe.world, new GameProfile(new UUID(0L, 0L), "_unknown"));
		}

		playerForStats.setWorld(team.universe.world);
		playerForStats.setUniqueId(getId());
		return team.universe.server.getPlayerList().getPlayerStatsFile(playerForStats);
	}

	public ConfigGroup getSettings()
	{
		if (cachedConfig == null)
		{
			cachedConfig = new ConfigGroup(new TextComponentTranslation("player_config"));
			cachedConfig.setSupergroup("player_config");
			ForgePlayerConfigEvent event = new ForgePlayerConfigEvent(this, cachedConfig);
			event.post();
			event.getConfig().setGroupName(FTBLib.MOD_ID, new TextComponentString(FTBLib.MOD_NAME));
			event.getConfig().add(FTBLib.MOD_ID, "hide_team_notification", hideTeamNotification);
		}

		return cachedConfig;
	}

	public IConfigCallback getConfigCallback()
	{
		if (cachedConfigCallback == null)
		{
			cachedConfigCallback = (group, sender, json) ->
			{
				group.fromJson(json);
				clearCache();
				markDirty();
			};
		}

		return cachedConfigCallback;
	}

	public NBTTagCompound getPlayerNBT()
	{
		if (isOnline())
		{
			return getPlayer().serializeNBT();
		}

		if (cachedPlayerNBT == null)
		{
			try
			{
				cachedPlayerNBT = FileUtils.readNBT(new File(team.universe.world.getSaveHandler().getWorldDirectory(), "playerdata/" + getId() + ".dat"));
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return cachedPlayerNBT;
	}

	public void setPlayerNBT(NBTTagCompound nbt)
	{
		//FIXME
		markDirty();
	}

	public boolean hideTeamNotification()
	{
		return FTBLibConfig.teams.hide_team_notification || hideTeamNotification.getBoolean() || isFake();
	}

	public long getLastTimeSeen()
	{
		return isOnline() ? team.universe.world.getTotalWorldTime() : lastTimeSeen;
	}

	public boolean hasPermission(String node, @Nullable IContext context)
	{
		return PermissionAPI.hasPermission(getProfile(), node, context);
	}

	public boolean hasPermission(String node)
	{
		return isOnline() ? PermissionAPI.hasPermission(getPlayer(), node) : PermissionAPI.hasPermission(getProfile(), node, null);
	}

	public ConfigValue getRankConfig(Node node)
	{
		return isOnline() ? RankConfigAPI.get(getPlayer(), node) : RankConfigAPI.get(team.universe.server, getProfile(), node, null);
	}

	public boolean isFirstLogin(ResourceLocation id)
	{
		if (!firstLogin.contains(id))
		{
			firstLogin.add(id);
			return true;
		}

		return false;
	}
}