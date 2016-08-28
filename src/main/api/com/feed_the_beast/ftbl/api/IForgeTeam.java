package com.feed_the_beast.ftbl.api;

import com.feed_the_beast.ftbl.api.security.EnumTeamPrivacyLevel;
import com.latmod.lib.annotations.IFlagContainer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.UUID;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IForgeTeam extends IStringSerializable, ICapabilitySerializable<NBTTagCompound>, IFlagContainer
{
    byte FREE_TO_JOIN = 1;
    byte HIDDEN = 2;

    IUniverse getUniverse();

    IForgePlayer getOwner();

    String getTitle();

    @Nullable
    String getDesc();

    EnumTeamColor getColor();

    EnumTeamStatus getStatus(@Nullable IForgePlayer player);

    boolean isAllyTeam(String team);

    boolean isEnemy(UUID player);

    Collection<IForgePlayer> getMembers();

    boolean isInvited(@Nullable IForgePlayer player);

    boolean canInteract(@Nullable IForgePlayer player, EnumTeamPrivacyLevel level);
}