package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.api.security.EnumPrivacyLevel;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IForgePlayer extends INBTSerializable<NBTTagCompound>
{
    int FLAG_HIDE_TEAM_NOTIFICATION = 1;
    int FLAG_HIDE_NEW_TEAM_MSG_NOTIFICATION = 2;

    UUID getId();

    String getName();

    default GameProfile getProfile()
    {
        return new GameProfile(getId(), getName());
    }

    EntityPlayerMP getPlayer();

    @Nullable
    INBTSerializable<?> getData(ResourceLocation id);

    boolean equalsPlayer(@Nullable IForgePlayer player);

    void setTeamID(String o);

    @Nullable
    IForgeTeam getTeam();

    default boolean canInteract(@Nullable IForgePlayer owner, EnumPrivacyLevel level)
    {
        if(level == EnumPrivacyLevel.PUBLIC || owner == null)
        {
            return true;
        }
        else if(owner.equalsPlayer(this))
        {
            return true;
        }
        else if(level == EnumPrivacyLevel.PRIVATE)
        {
            return false;
        }
        else if(level == EnumPrivacyLevel.TEAM)
        {
            IForgeTeam team = owner.getTeam();

            if(team != null)
            {
                if(team.hasStatus(this, EnumTeamStatus.ALLY))
                {
                    return true;
                }
            }
        }

        return false;
    }

    boolean isOnline();

    boolean isFake();

    boolean isOP();

    StatisticsManagerServer stats();

    IConfigTree getSettings();

    NBTTagCompound getPlayerNBT();

    int getFlags();

    void setFlags(int flags);
}