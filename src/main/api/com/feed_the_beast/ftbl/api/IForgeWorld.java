package com.feed_the_beast.ftbl.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import java.util.Collection;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IForgeWorld extends ICapabilitySerializable<NBTTagCompound>
{
    Collection<? extends IForgePlayer> getPlayers();

    IForgePlayer getPlayer(Object o);

    IForgePlayer getCurrentPlayer();

    Collection<? extends IForgeTeam> getTeams();

    IForgeTeam getTeam(String id);

    IForgeTeam getCurrentTeam();
}