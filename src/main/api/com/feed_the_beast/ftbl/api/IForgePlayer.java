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

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IForgePlayer extends INBTSerializable<NBTTagCompound>
{
    byte FLAG_HIDE_TEAM_NOTIFICATION = 1;

    GameProfile getProfile();

    @Nullable
    EntityPlayerMP getPlayer();

    @Nullable
    INBTSerializable<?> getData(ResourceLocation id);

    boolean equalsPlayer(@Nullable IForgePlayer player);

    String getTeamID();

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

    void getSettings(IConfigTree tree);

    NBTTagCompound getPlayerNBT();

    byte getFlags();

    void setFlags(byte flags);
}