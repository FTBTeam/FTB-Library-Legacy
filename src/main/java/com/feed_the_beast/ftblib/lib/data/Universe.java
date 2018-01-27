package com.feed_the_beast.ftblib.lib.data;

import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.FTBLibFinals;
import com.feed_the_beast.ftblib.FTBLibLang;
import com.feed_the_beast.ftblib.events.ServerReloadEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftblib.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseClosedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseLoadedEvent;
import com.feed_the_beast.ftblib.events.universe.UniverseSavedEvent;
import com.feed_the_beast.ftblib.lib.EnumReloadType;
import com.feed_the_beast.ftblib.lib.EnumTeamStatus;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import com.feed_the_beast.ftblib.lib.util.FileUtils;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.feed_the_beast.ftblib.lib.util.NBTUtils;
import com.feed_the_beast.ftblib.lib.util.ServerUtils;
import com.feed_the_beast.ftblib.lib.util.StringUtils;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * @author LatvianModder
 */
@Mod.EventBusSubscriber(modid = FTBLibFinals.MOD_ID)
public class Universe
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
		if (event.getWorld().provider.getDimension() != 0 || event.getWorld().getMinecraftServer() == null)
		{
			return;
		}

		ServerUtils.setServer(event.getWorld().getMinecraftServer());
		SharedServerData.INSTANCE.reset();
		CommonUtils.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), ServerUtils.getServer().getFolderName());

		INSTANCE = new Universe();
		INSTANCE.load((WorldServer) event.getWorld());
	}

	private void load(WorldServer world)
	{
		File folder = new File(CommonUtils.folderWorld, "data/ftb_lib/");

		NBTTagCompound data = new NBTTagCompound();
		JsonObject jsonWorldData = new JsonObject();

		try
		{
			JsonElement worldData = JsonUtils.fromJson(new File(CommonUtils.folderWorld, "world_data.json"));

			if (worldData.isJsonObject())
			{
				jsonWorldData = worldData.getAsJsonObject();
			}

			NBTTagCompound universeData = NBTUtils.readTag(new File(folder, "universe.dat"));
			data = universeData == null ? new NBTTagCompound() : universeData.getCompoundTag("Data");
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		SharedServerData.INSTANCE.fromJson(jsonWorldData);
		new UniverseLoadedEvent.Pre(world, data).post();

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
						NBTTagCompound nbt = NBTUtils.readTag(f);

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
								ForgePlayer player = new ForgePlayer(uuid, nbt.getString("Name"));
								player.firstLogin = false;
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
				for (File f : files)
				{
					if (f.getName().endsWith(".dat"))
					{
						NBTTagCompound nbt = NBTUtils.readTag(f);

						if (nbt != null)
						{
							String s = FileUtils.getRawFileName(f);
							teamNBT.put(s, nbt);
							teams.put(s, new ForgeTeam(s));
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		for (ForgePlayer player : players.values())
		{
			NBTTagCompound nbt = playerNBT.get(player.getId());

			player.hideTeamNotification.setBoolean(nbt.getBoolean("HideTeamNotification"));
			player.lastTimeSeen = nbt.getLong("LastTimeSeen");
			player.setTeamId(nbt.getString("TeamID"));
			player.dataStorage.deserializeNBT(nbt.getCompoundTag("Data"));
		}

		players.put(ForgePlayerFake.SERVER.getId(), ForgePlayerFake.SERVER);

		Iterator<ForgeTeam> teamIterator = teams.values().iterator();

		while (teamIterator.hasNext())
		{
			ForgeTeam team = teamIterator.next();
			NBTTagCompound nbt = teamNBT.get(team.getName());

			team.owner = getPlayer(nbt.getString("Owner"));

			if (team.owner == null)
			{
				teamIterator.remove();
				continue;
			}

			team.color.setValueFromString(nbt.getString("Color"), false);
			team.fakePlayerStatus.setValueFromString(nbt.getString("FakePlayerStatus"), false);
			team.title.setString(nbt.getString("Title"));
			team.desc.setString(nbt.getString("Desc"));
			team.freeToJoin.setBoolean(nbt.getBoolean("FreeToJoin"));

			team.players.clear();

			if (nbt.hasKey("Players"))
			{
				NBTTagCompound nbt1 = nbt.getCompoundTag("Players");

				for (String s : nbt1.getKeySet())
				{
					ForgePlayer player = getPlayer(s);

					if (player != null)
					{
						EnumTeamStatus status = EnumTeamStatus.NAME_MAP.get(nbt1.getString(s));

						if (status.canBeSet())
						{
							team.setStatus(player, status);
						}
					}
				}
			}

			NBTTagList list = nbt.getTagList("RequestingInvite", Constants.NBT.TAG_STRING);

			for (int i = 0; i < list.tagCount(); i++)
			{
				ForgePlayer player = getPlayer(list.getStringTagAt(i));

				if (player != null && !team.isMember(player))
				{
					team.setRequestingInvite(player, true);
				}
			}

			list = nbt.getTagList("Invited", Constants.NBT.TAG_STRING);

			for (int i = 0; i < list.tagCount(); i++)
			{
				ForgePlayer player = getPlayer(list.getStringTagAt(i));

				if (player != null && !team.isMember(player))
				{
					team.setStatus(player, EnumTeamStatus.INVITED);
				}
			}

			team.dataStorage.deserializeNBT(nbt.getCompoundTag("Data"));
		}

		new UniverseLoadedEvent.Post(world, data).post();
		new UniverseLoadedEvent.Finished(world).post();

		FTBLibAPI.reloadServer(ServerUtils.getServer(), EnumReloadType.CREATED, ServerReloadEvent.ALL);
	}

	@SubscribeEvent
	public static void onWorldUnloaded(WorldEvent.Unload event)
	{
		MinecraftServer server = event.getWorld().getMinecraftServer();

		if (server != null && INSTANCE != null && event.getWorld().provider.getDimension() == 0)
		{
			for (ForgePlayer player : INSTANCE.getPlayers())
			{
				player.onLoggedOut();
			}

			new UniverseClosedEvent().post();
			INSTANCE.players.clear();
			INSTANCE.teams.clear();
			INSTANCE = null;
		}
	}

	@SubscribeEvent
	public static void onWorldSaved(WorldEvent.Save event)
	{
		MinecraftServer server = event.getWorld().getMinecraftServer();

		if (Universe.INSTANCE != null && server != null && event.getWorld().provider.getDimension() == 0)
		{
			try
			{
				Universe.INSTANCE.save();
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}

	private void save() throws Exception
	{
		JsonUtils.toJson(SharedServerData.INSTANCE.getSerializableElement(), new File(CommonUtils.folderWorld, "world_data.json"));
		File folder = new File(CommonUtils.folderWorld, "data/ftb_lib");

		NBTTagCompound mainNbt = new NBTTagCompound();
		NBTTagCompound data = new NBTTagCompound();
		new UniverseSavedEvent(data).post();
		mainNbt.setTag("Data", data);
		NBTUtils.writeTag(new File(folder, "universe.dat"), mainNbt);

		for (ForgePlayer player : players.values())
		{
			if (!player.isFake())
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setBoolean("HideTeamNotification", player.hideTeamNotification.getBoolean());
				nbt.setLong("LastTimeSeen", player.lastTimeSeen);
				nbt.setString("TeamID", (player.team != null && player.team.isValid()) ? player.team.getName() : "");
				nbt.setTag("Data", player.dataStorage.serializeNBT());
				nbt.setString("Name", player.getName());
				nbt.setString("UUID", StringUtils.fromUUID(player.getId()));

				NBTUtils.writeTag(new File(folder, "players/" + player.getName().toLowerCase() + ".dat"), nbt);
			}
		}

		for (ForgeTeam team : teams.values())
		{
			NBTTagCompound nbt = new NBTTagCompound();

			nbt.setString("Owner", team.owner.getName());
			nbt.setString("Color", team.color.getString());
			nbt.setString("FakePlayerStatus", team.fakePlayerStatus.getString());
			nbt.setString("Title", team.title.getString());
			nbt.setString("Desc", team.desc.getString());
			nbt.setBoolean("FreeToJoin", team.freeToJoin.getBoolean());

			NBTTagCompound nbt1 = new NBTTagCompound();

			if (!team.players.isEmpty())
			{
				for (Map.Entry<ForgePlayer, EnumTeamStatus> entry : team.players.entrySet())
				{
					nbt1.setString(entry.getKey().getName(), entry.getValue().getName());
				}
			}

			nbt.setTag("Players", nbt1);

			NBTTagList list = new NBTTagList();

			for (ForgePlayer player : team.requestingInvite)
			{
				list.appendTag(new NBTTagString(player.getName()));
			}

			nbt.setTag("RequestingInvite", list);

			nbt.setTag("Data", team.dataStorage.serializeNBT());

			NBTUtils.writeTag(new File(folder, "teams/" + team.getName() + ".dat"), nbt);
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event)
	{
		if (!(event.player instanceof EntityPlayerMP) || event.player instanceof FakePlayer)
		{
			return;
		}

		EntityPlayerMP player = (EntityPlayerMP) event.player;

		if (!player.mcServer.getPlayerList().canJoin(player.getGameProfile()))
		{
			return;
		}

		ForgePlayer p = INSTANCE.getPlayer(player.getGameProfile());
		boolean firstLogin = p == null;

		if (firstLogin)
		{
			p = new ForgePlayer(player.getUniqueID(), player.getName());
			INSTANCE.players.put(p.getId(), p);
		}
		else if (!p.getName().equals(player.getName()))
		{
			p.setUsername(player.getName());
		}

		p.onLoggedIn(player, firstLogin);

		if (firstLogin && (player.mcServer.isDedicatedServer() ? FTBLibConfig.teams.autocreate_mp : FTBLibConfig.teams.autocreate_sp))
		{
			if (player.mcServer.isDedicatedServer())
			{
				String id = p.getName().toLowerCase();

				if (INSTANCE.getTeam(id) != null)
				{
					id = StringUtils.fromUUID(p.getId());
				}

				if (INSTANCE.getTeam(id) == null)
				{
					ForgeTeam team = new ForgeTeam(id);
					INSTANCE.teams.put(team.getName(), team);
					p.setTeamId(team.getName());
					team.setStatus(p, EnumTeamStatus.OWNER);
					new ForgeTeamCreatedEvent(team).post();
					new ForgeTeamPlayerJoinedEvent(team, p).post();
				}
			}
			else
			{
				ForgeTeam team = INSTANCE.getTeam("singleplayer");

				if (team == null)
				{
					team = new ForgeTeam("singleplayer");
					INSTANCE.teams.put(team.getName(), team);
					p.setTeamId(team.getName());
					team.setStatus(p, EnumTeamStatus.OWNER);
					new ForgeTeamCreatedEvent(team).post();
					new ForgeTeamPlayerJoinedEvent(team, p).post();
				}
				else
				{
					p.setTeamId(team.getName());
					new ForgeTeamPlayerJoinedEvent(team, p).post();
				}
			}
		}

		if (!p.hideTeamNotification() && p.getTeam() == null)
		{
			ITextComponent b1 = FTBLibLang.CLICK_HERE.textComponent(player);
			b1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb team gui"));
			b1.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.MY_TEAM.textComponent(player)));
			ITextComponent b2 = FTBLibLang.CLICK_HERE.textComponent(player);
			b2.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb my_settings " + FTBLibFinals.MOD_ID + ".hide_team_notification toggle"));
			b2.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.TEAM_NOTIFICATION_HIDE.textComponent(player)));
			FTBLibLang.TEAM_NOTIFICATION.sendMessage(player, b1, b2);
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if (event.player instanceof EntityPlayerMP && loaded())
		{
			ForgePlayer player = INSTANCE.getPlayer(event.player.getGameProfile());

			if (player != null)
			{
				player.onLoggedOut();
			}
		}
	}

	// Event handler end //

	public final Map<UUID, ForgePlayer> players;
	public final Map<String, ForgeTeam> teams;

	public Universe()
	{
		players = new HashMap<>();
		teams = new HashMap<>();
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
		if (sender == ServerUtils.getServer())
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

	public ForgePlayer getPlayer(ForgePlayer player)
	{
		ForgePlayer p = getPlayer(player.getId());
		return p == null ? player : p;
	}

	@Nullable
	public ForgePlayer getPlayer(GameProfile profile)
	{
		ForgePlayer player = getPlayer(profile.getId());

		if (player == null && FTBLibConfig.general.merge_offline_mode_players.get(!ServerUtils.getServer().isDedicatedServer()))
		{
			player = getPlayer(profile.getName());

			if (player != null)
			{
				players.put(profile.getId(), player);
			}
		}

		return player;
	}

	public Collection<ForgeTeam> getTeams()
	{
		return teams.values();
	}

	@Nullable
	public ForgeTeam getTeam(String id)
	{
		return id.isEmpty() ? null : teams.get(id);
	}

	public Collection<ForgePlayer> getOnlinePlayers()
	{
		Collection<ForgePlayer> l = Collections.emptySet();

		for (ForgePlayer p : players.values())
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