package com.feed_the_beast.ftbl.api_impl;

import com.feed_the_beast.ftbl.FTBLibConfig;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.IUniverse;
import com.feed_the_beast.ftbl.api.universe.ForgeUniverseClosedEvent;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class Universe implements IUniverse
{
	public static Universe INSTANCE = null;

	private final WorldServer world;
	public final Map<UUID, ForgePlayer> players;
	public final Map<String, ForgeTeam> teams;

	public Universe(WorldServer w)
	{
		world = w;
		players = new HashMap<>();
		teams = new HashMap<>();
	}

	@Override
	public WorldServer getOverworld()
	{
		return world;
	}

	@Override
	public Collection<ForgePlayer> getPlayers()
	{
		return players.values();
	}

	@Override
	@Nullable
	public ForgePlayer getPlayer(@Nullable UUID id)
	{
		return (id == null || id.getLeastSignificantBits() == 0L && id.getMostSignificantBits() == 0L) ? null : players.get(id);
	}

	@Override
	@Nullable
	public ForgePlayer getPlayer(CharSequence nameOrId)
	{
		String s = nameOrId.toString().toLowerCase();

		if (s.isEmpty())
		{
			return null;
		}

		UUID id = StringUtils.fromString(s);

		if (id != null)
		{
			return getPlayer(id);
		}

		for (ForgePlayer p : players.values())
		{
			if (p.getName().toLowerCase().equals(s))
			{
				return p;
			}
		}

		for (ForgePlayer p : players.values())
		{
			if (p.getName().toLowerCase().contains(s))
			{
				return p;
			}
		}

		return null;
	}

	@Override
	public ForgePlayer getPlayer(ICommandSender sender)
	{
		if (sender == getServer())
		{
			return ForgePlayerFake.SERVER;
		}

		Preconditions.checkArgument(sender instanceof EntityPlayerMP);
		EntityPlayerMP player = (EntityPlayerMP) sender;
		ForgePlayer p = getPlayer(player.getGameProfile());

		if (p == null && player instanceof FakePlayer)
		{
			p = new ForgePlayerFake((FakePlayer) player);
			players.put(p.getId(), p);
			p.onLoggedIn(player, false);
			return p;
		}

		return Objects.requireNonNull(p);
	}

	@Override
	@Nullable
	public ForgePlayer getPlayer(GameProfile profile)
	{
		ForgePlayer player = getPlayer(profile.getId());

		if (player == null && FTBLibConfig.general.merge_offline_mode_players.get(!getServer().isDedicatedServer()))
		{
			player = getPlayer(profile.getName());

			if (player != null)
			{
				players.put(profile.getId(), player);
			}
		}

		return player;
	}

	@Override
	public Collection<ForgeTeam> getTeams()
	{
		return teams.values();
	}

	@Override
	@Nullable
	public ForgeTeam getTeam(String id)
	{
		return id.isEmpty() ? null : teams.get(id);
	}

	public void onClosed()
	{
		new ForgeUniverseClosedEvent(this).post();
		players.clear();
		teams.clear();
	}

	@Override
	public Collection<IForgePlayer> getOnlinePlayers()
	{
		Collection<IForgePlayer> l = Collections.emptySet();

		for (IForgePlayer p : players.values())
		{
			if (p.isOnline())
			{
				if (l.isEmpty())
				{
					l = new HashSet<>();
				}

				l.add(p);
			}
		}

		return l;
	}
}
