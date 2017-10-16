package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.util.misc.EnumPrivacyLevel;
import com.feed_the_beast.ftbl.lib.util.misc.NBTDataStorage;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public interface IForgePlayer extends IStringSerializable
{
	UUID getId();

	default GameProfile getProfile()
	{
		return new GameProfile(getId(), getName());
	}

	EntityPlayerMP getPlayer();

	NBTDataStorage getData();

	default boolean equalsPlayer(@Nullable IForgePlayer player)
	{
		return player == this || (player != null && getId().equals(player.getId()));
	}

	void setTeamId(String o);

	@Nullable
	IForgeTeam getTeam();

	default boolean canInteract(@Nullable IForgePlayer owner, EnumPrivacyLevel level)
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
			IForgeTeam team = owner.getTeam();

			if (team != null && team.isAlly(this))
			{
				return true;
			}
		}

		return false;
	}

	boolean isOnline();

	boolean isFake();

	boolean isOP();

	StatisticsManagerServer stats();

	ConfigGroup getSettings();

	NBTTagCompound getPlayerNBT();

	void setPlayerNBT(NBTTagCompound nbt);

	boolean hideTeamNotification();

	boolean isLoggingOut();

	long getLastTimeSeen();
}