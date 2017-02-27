package com.feed_the_beast.ftbl.api;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * Created by LatvianModder on 11.08.2016.
 */
public interface IUniverse
{
    @Nullable
    INBTSerializable<?> getData(ResourceLocation id);

    Collection<? extends IForgePlayer> getPlayers();

    @Nullable
    IForgePlayer getPlayer(@Nullable Object o);

    Collection<? extends IForgeTeam> getTeams();

    @Nullable
    IForgeTeam getTeam(String id);

    Collection<IForgePlayer> getOnlinePlayers();
}