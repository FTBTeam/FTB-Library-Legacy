package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.security.EnumPrivacyLevel;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IForgePlayer extends ICapabilitySerializable<NBTTagCompound>
{
    GameProfile getProfile();

    IUniverse getUniverse();

    @Nullable
    EntityPlayerMP getPlayer();

    boolean equalsPlayer(@Nullable IForgePlayer player);

    @Nullable
    String getTeamID();

    @Nullable
    IForgeTeam getTeam();

    default boolean isMemberOf(@Nullable ForgeTeam team)
    {
        String teamID = getTeamID();
        return teamID != null && team != null && team.getName().equals(teamID);
    }

    boolean canInteract(@Nullable IForgePlayer owner, EnumPrivacyLevel level);

    boolean isOnline();

    boolean isFake();

    boolean isMemberOf(IForgeTeam team);

    boolean isOP();

    StatisticsManagerServer stats();
}