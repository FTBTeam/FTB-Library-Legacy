package com.feed_the_beast.ftbl.api;

import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public interface IUniverse
{
	default MinecraftServer getServer()
	{
		return Objects.requireNonNull(getOverworld().getMinecraftServer());
	}

	WorldServer getOverworld();

	Collection<? extends IForgePlayer> getPlayers();

	default List<IForgePlayer> getRealPlayers()
	{
		List<IForgePlayer> list = new ArrayList<>();

		for (IForgePlayer player : getPlayers())
		{
			if (!player.isFake())
			{
				list.add(player);
			}
		}

		return list;
	}

	@Nullable
	IForgePlayer getPlayer(@Nullable UUID id);

	@Nullable
	IForgePlayer getPlayer(CharSequence nameOrId);

	IForgePlayer getPlayer(ICommandSender player);

	default IForgePlayer getPlayer(IForgePlayer player)
	{
		IForgePlayer p = getPlayer(player.getId());
		return p == null ? player : p;
	}

	@Nullable
	default IForgePlayer getPlayer(GameProfile profile)
	{
		return getPlayer(profile.getId());
	}

	Collection<? extends IForgeTeam> getTeams();

	@Nullable
	IForgeTeam getTeam(String id);

	Collection<IForgePlayer> getOnlinePlayers();
}