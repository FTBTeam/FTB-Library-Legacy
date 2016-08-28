package com.feed_the_beast.ftbl.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IForgeWorld extends ICapabilitySerializable<NBTTagCompound>
{
    Collection<? extends IForgePlayer> getPlayers();

    @Nullable
    IForgePlayer getPlayer(@Nullable Object o);

    @Nullable
    IForgePlayer getCurrentPlayer();

    Collection<? extends IForgeTeam> getTeams();

    @Nullable
    IForgeTeam getTeam(String id);

    @Nullable
    IForgeTeam getCurrentTeam();
}