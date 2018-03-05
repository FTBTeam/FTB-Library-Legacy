package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.events.RegisterOptionalServerModsEvent;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.events.player.ForgePlayerLoggedInEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseClosedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseSavedEvent;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.util.FileUtils;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.feed_the_beast.ftblib.net.MessageSyncData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ThreadedFileIOBase;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBLib.MOD_ID)
public class Universe implements IHasCache
{
	private static Universe INSTANCE = null;

	public static boolean loaded()
	{
		return INSTANCE != null;
	}

	public static Universe get()
	{
		if (INSTANCE == null)
		{
			throw new NullPointerException("FTBLib Universe == null!");
		}

		return INSTANCE;
	}

	// Event handlers start //

	@SubscribeEvent
	public static void onWorldLoaded(WorldEvent.Load event)
	{
		if (event.getWorld().provider.getDimension() == 0 && !event.getWorld().isRemote)
		{
			INSTANCE = new Universe((WorldServer) event.getWorld());
			INSTANCE.load();
		}
	}

	@SubscribeEvent
	public static void onWorldUnloaded(WorldEvent.Unload event)
	{
		if (loaded() && event.getWorld() == INSTANCE.world)
		{
			for (ForgePlayer player : INSTANCE.getPlayers())
			{
				player.onLoggedOut();
			}

			new UniverseClosedEvent(INSTANCE).post();
			INSTANCE.save();
			INSTANCE = null;
		}
	}

	@SubscribeEvent
	public static void onWorldSaved(WorldEvent.Save event)
	{
		if (loaded() && event.getWorld() == INSTANCE.world)
		{
			INSTANCE.save();
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if (loaded() && event.player instanceof EntityPlayerMP && !(event.player instanceof FakePlayer))
		{
			INSTANCE.onPlayerLoggedIn((EntityPlayerMP) event.player);
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if (loaded() && event.player instanceof EntityPlayerMP)
		{
			INSTANCE.onPlayerLoggedOut((EntityPlayerMP) event.player);
		}
	}

	// Event handler end //

	@Nonnull
	public final MinecraftServer server;
	public final WorldServer world;
	public final Map<UUID, ForgePlayer> players;
	public final Map<String, ForgeTeam> teams;
	private final ForgeTeam noneTeam;
	private UUID uuid;
	public final Collection<String> optionalServerMods;
	public boolean needsSaving;
	public boolean checkSaving;

	@SuppressWarnings("ConstantConditions")
	public Universe(WorldServer w)
	{
		server = w.getMinecraftServer();
		world = w;
		players = new HashMap<>();
		teams = new HashMap<>();
		noneTeam = new ForgeTeam(this, "", TeamType.NONE);
		uuid = null;
		optionalServerMods = new HashSet<>();
		needsSaving = false;
		checkSaving = true;
	}

	public void markDirty()
	{
		needsSaving = true;
		checkSaving = true;
	}

	public UUID getUUID()
	{
		if (uuid == null)
		{
			uuid = UUID.randomUUID();
			markDirty();
		}

		return uuid;
	}

	private void load()
	{
		File folder = new File(world.getSaveHandler().getWorldDirectory(), "data/ftb_lib/");
		NBTTagCompound universeData = FileUtils.readNBT(new File(folder, "universe.dat"));

		if (universeData == null)
		{
			universeData = new NBTTagCompound();
		}

		File worldDataJsonFile = new File(world.getSaveHandler().getWorldDirectory(), "world_data.json");
		JsonElement worldData = JsonUtils.fromJson(worldDataJsonFile);

		if (worldData.isJsonObject())
		{
			JsonObject jsonWorldData = worldData.getAsJsonObject();

			if (jsonWorldData.has("world_id"))
			{
				universeData.setString("UUID", jsonWorldData.get("world_id").getAsString());
			}

			worldDataJsonFile.delete();
		}

		uuid = StringUtils.fromString(universeData.getString("UUID"));

		if (uuid != null && uuid.getLeastSignificantBits() == 0L && uuid.getMostSignificantBits() == 0L)
		{
			uuid = null;
		}

		NBTTagCompound data = universeData.getCompoundTag("Data");

		optionalServerMods.clear();
		new RegisterOptionalServerModsEvent(optionalServerMods::add).post();
		new UniverseLoadedEvent.Pre(this, world, data).post();

		Map<UUID, NBTTagCompound> playerNBT = new HashMap<>();
		Map<String, NBTTagCompound> teamNBT = new HashMap<>();

		try
		{
			File[] files = new File(folder, "players").listFiles();

			if (files != null && files.length > 0)
			{
				for (File f : files)
				{
					if (f.getName().endsWith(".dat"))
					{
						NBTTagCompound nbt = FileUtils.readNBT(f);

						if (nbt != null)
						{
							String uuidString = nbt.getString("UUID");

							if (uuidString.isEmpty())
							{
								uuidString = FileUtils.getRawFileName(f);
								FileUtils.delete(f);
							}

							UUID uuid = StringUtils.fromString(uuidString);

							if (uuid != null)
							{
								playerNBT.put(uuid, nbt);
								ForgePlayer player = new ForgePlayer(this, uuid, nbt.getString("Name"));
								players.put(uuid, player);
							}
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		try
		{
			File[] files = new File(folder, "teams").listFiles();

			if (files != null && files.length > 0)
			{
				for (File file : files)
				{
					if (file.getName().endsWith(".dat"))
					{
						NBTTagCompound nbt = FileUtils.readNBT(file);

						if (nbt != null)
						{
							String s = FileUtils.getRawFileName(file);
							teamNBT.put(s, nbt);
							teams.put(s, new ForgeTeam(this, s, TeamType.NAME_MAP.get(nbt.getString("Type"))));
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		new UniverseLoadedEvent.CreateServerTeams(this, world).post();

		for (ForgePlayer player : players.values())
		{
			NBTTagCompound nbt = playerNBT.get(player.getId());

			if (nbt != null && !nbt.hasNoTags())
			{
				player.team = getTeam(nbt.getString("TeamID"));
				player.deserializeNBT(nbt);
			}
		}

		for (ForgeTeam team : teams.values())
		{
			if (!team.type.save)
			{
				continue;
			}

			NBTTagCompound nbt = teamNBT.get(team.getName());

			if (nbt != null && !nbt.hasNoTags())
			{
				team.deserializeNBT(nbt);
			}
		}

		new UniverseLoadedEvent.Post(this, world, data).post();
		new UniverseLoadedEvent.Finished(this, world).post();

		FTBLibAPI.reloadServer(this, server, EnumReloadType.CREATED, ServerReloadEvent.ALL);
	}

	private void save()
	{
		if (!checkSaving)
		{
			return;
		}

		final File worldDirectory = world.getSaveHandler().getWorldDirectory();
		final NBTTagCompound universeData = new NBTTagCompound();
		final Map<String, NBTTagCompound> playerDataMap = new HashMap<>();
		final Map<String, NBTTagCompound> teamDataMap = new HashMap<>();

		if (needsSaving)
		{
			NBTTagCompound data = new NBTTagCompound();
			new UniverseSavedEvent(this, data).post();
			universeData.setTag("Data", data);
			universeData.setString("UUID", StringUtils.fromUUID(getUUID()));
			needsSaving = false;
		}

		for (ForgePlayer player : players.values())
		{
			if (player.needsSaving)
			{
				if (!player.isFake())
				{
					NBTTagCompound nbt = player.serializeNBT();
					nbt.setString("Name", player.getName());
					nbt.setString("UUID", StringUtils.fromUUID(player.getId()));
					nbt.setString("TeamID", player.team.getName());
					playerDataMap.put(player.getName().toLowerCase(), nbt);
				}

				player.needsSaving = false;
			}
		}

		for (ForgeTeam team : teams.values())
		{
			if (team.needsSaving)
			{
				if (team.type.save && team.isValid())
				{
					NBTTagCompound nbt = team.serializeNBT();
					nbt.setString("Type", team.type.getName());
					teamDataMap.put(team.getName(), nbt);
					team.needsSaving = false;
				}
				else
				{
					File file = new File(worldDirectory, "data/ftb_lib/teams/" + team.getName() + ".dat");

					if (file.exists())
					{
						file.delete();
					}
				}

				team.needsSaving = false;
			}
		}

		if (!universeData.hasNoTags() || !playerDataMap.isEmpty() || !teamDataMap.isEmpty())
		{
			ThreadedFileIOBase.getThreadedIOInstance().queueIO(() ->
			{
				if (FTBLibConfig.debugging.print_more_info)
				{
					FTBLib.LOGGER.info("Saving data");
				}

				File folder = new File(worldDirectory, "data/ftb_lib");

				if (!universeData.hasNoTags())
				{
					FileUtils.writeNBT(new File(folder, "universe.dat"), universeData);

					if (FTBLibConfig.debugging.print_more_info)
					{
						FTBLib.LOGGER.info("Saved universe data");
					}
				}

				for (Map.Entry<String, NBTTagCompound> entry : playerDataMap.entrySet())
				{
					FileUtils.writeNBT(new File(folder, "players/" + entry.getKey() + ".dat"), entry.getValue());

					if (FTBLibConfig.debugging.print_more_info)
					{
						FTBLib.LOGGER.info("Saved player data for " + entry.getKey());
					}
				}

				for (Map.Entry<String, NBTTagCompound> entry : teamDataMap.entrySet())
				{
					FileUtils.writeNBT(new File(folder, "teams/" + entry.getKey() + ".dat"), entry.getValue());

					if (FTBLibConfig.debugging.print_more_info)
					{
						FTBLib.LOGGER.info("Saved team data for " + entry.getKey());
					}
				}

				return false;
			});
		}

		checkSaving = false;
	}

	private void onPlayerLoggedIn(EntityPlayerMP player)
	{
		if (!player.mcServer.getPlayerList().canJoin(player.getGameProfile()))
		{
			return;
		}

		ForgePlayer p = getPlayer(player.getGameProfile());
		boolean firstLogin = p == null;

		if (firstLogin)
		{
			p = new ForgePlayer(this, player.getUniqueID(), player.getName());
			players.put(p.getId(), p);
		}
		else if (!p.getName().equals(player.getName()))
		{
			p.setName(player.getName());
		}

		boolean sendTeamJoinEvent = false, sendTeamCreatedEvent = false;

		if (firstLogin && (player.mcServer.isDedicatedServer() ? FTBLibConfig.teams.autocreate_mp : FTBLibConfig.teams.autocreate_sp))
		{
			if (player.mcServer.isDedicatedServer())
			{
				String id = p.getName().toLowerCase();

				if (getTeam(id).isValid())
				{
					id = StringUtils.fromUUID(p.getId());
				}

				if (!getTeam(id).isValid())
				{
					ForgeTeam team = new ForgeTeam(this, id, TeamType.PLAYER);
					team.owner = p;
					teams.put(team.getName(), team);
					p.team = team;
					team.markDirty();
					sendTeamCreatedEvent = true;
					sendTeamJoinEvent = true;
				}
			}
			else
			{
				ForgeTeam team = getTeam("singleplayer");

				if (!team.isValid())
				{
					team = new ForgeTeam(this, "singleplayer", TeamType.PLAYER);
					team.setFreeToJoin(true);
					team.owner = p;
					teams.put(team.getName(), team);
					p.team = team;
					team.markDirty();
					sendTeamCreatedEvent = true;
				}
				else
				{
					p.team = team;
				}

				sendTeamJoinEvent = true;
			}
		}

		p.entityPlayer = player;
		p.clearCache();

		if (!p.isFake())
		{
			p.lastTimeSeen = p.team.universe.world.getTotalWorldTime();
			//FTBLibStats.updateLastSeen(stats());
			new MessageSyncData(true, player, p).sendTo(p.entityPlayer);
		}

		new ForgePlayerLoggedInEvent(p).post();

		if (sendTeamCreatedEvent)
		{
			new ForgeTeamCreatedEvent(p.team).post();
		}

		if (sendTeamJoinEvent)
		{
			new ForgeTeamPlayerJoinedEvent(p).post();
		}

		if (!p.hideTeamNotification() && !p.hasTeam())
		{
			ITextComponent b1 = FTBLibLang.CLICK_HERE.textComponent(player);
			b1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb team gui"));
			b1.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.MY_TEAM.textComponent(player)));
			ITextComponent b2 = FTBLibLang.CLICK_HERE.textComponent(player);
			b2.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb my_settings " + FTBLib.MOD_ID + ".hide_team_notification toggle"));
			b2.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.TEAM_NOTIFICATION_HIDE.textComponent(player)));
			FTBLibLang.TEAM_NOTIFICATION.sendMessage(player, b1, b2);
		}

		p.markDirty();
	}

	private void onPlayerLoggedOut(EntityPlayerMP player)
	{
		ForgePlayer p = INSTANCE.getPlayer(player.getGameProfile());

		if (p != null)
		{
			p.onLoggedOut();
		}
	}

	public Collection<ForgePlayer> getPlayers()
	{
		return players.values();
	}

	public List<ForgePlayer> getRealPlayers()
	{
		List<ForgePlayer> list = new ArrayList<>();

		for (ForgePlayer player : getPlayers())
		{
			if (!player.isFake())
			{
				list.add(player);
			}
		}

		return list;
	}

	@Nullable
	public ForgePlayer getPlayer(@Nullable UUID id)
	{
		return (id == null || id.getLeastSignificantBits() == 0L && id.getMostSignificantBits() == 0L) ? null : players.get(id);
	}

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

	public ForgePlayer getPlayer(ICommandSender sender)
	{
		EntityPlayerMP player = (EntityPlayerMP) sender;
		ForgePlayer p = getPlayer(player.getGameProfile());

		if (p == null && player instanceof FakePlayer)
		{
			p = new ForgePlayer(this, player.getUniqueID(), player.getName());
			p.entityPlayer = player;
			p.clearCache();
			players.put(p.getId(), p);
			new ForgePlayerLoggedInEvent(p).post();
			p.markDirty();
			return p;
		}

		return Objects.requireNonNull(p);
	}

	public ForgePlayer getPlayer(ForgePlayer player)
	{
		ForgePlayer p = getPlayer(player.getId());
		return p == null ? player : p;
	}

	@Nullable
	public ForgePlayer getPlayer(GameProfile profile)
	{
		ForgePlayer player = getPlayer(profile.getId());

		if (player == null && FTBLibConfig.general.merge_offline_mode_players.get(!server.isDedicatedServer()))
		{
			player = getPlayer(profile.getName());

			if (player != null)
			{
				players.put(profile.getId(), player);
				player.markDirty();
			}
		}

		return player;
	}

	public Collection<ForgeTeam> getTeams()
	{
		return teams.values();
	}

	public ForgeTeam getTeam(String id)
	{
		ForgeTeam team = id.isEmpty() ? null : teams.get(id);
		return team == null ? noneTeam : team;
	}

	public Collection<ForgePlayer> getOnlinePlayers()
	{
		Collection<ForgePlayer> set = Collections.emptySet();

		for (ForgePlayer player : players.values())
		{
			if (player.isOnline())
			{
				if (set.isEmpty())
				{
					set = new HashSet<>();
				}

				set.add(player);
			}
		}

		return set;
	}

	@Override
	public void clearCache()
	{
		for (ForgeTeam team : teams.values())
		{
			team.clearCache();
		}

		for (ForgePlayer player : players.values())
		{
			player.clearCache();
		}
	}
}