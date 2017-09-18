package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.ITeamMessage;
import com.feed_the_beast.ftbl.api.events.ReloadEvent;
import com.feed_the_beast.ftbl.api.events.player.ForgePlayerLoggedOutEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.events.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniverseLoadedEvent;
import com.feed_the_beast.ftbl.api.events.universe.ForgeUniverseSavedEvent;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgePlayerFake;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
import com.feed_the_beast.ftbl.lib.io.Bits;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.FileUtils;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.NBTUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author LatvianModder
 */
@EventHandler
public class FTBLibEventHandler
{
	public static final ResourceLocation RELOAD_CONFIG = FTBLibFinals.get("config");

	@SubscribeEvent
	public static void onWorldLoaded(WorldEvent.Load event)
	{
		MinecraftServer server = event.getWorld().getMinecraftServer();

		if (event.getWorld().provider.getDimension() != 0 || server == null)
		{
			return;
		}

		SharedServerData.INSTANCE.reset();
		CommonUtils.folderWorld = new File(FMLCommonHandler.instance().getSavesDirectory(), server.getFolderName());

		Universe.INSTANCE = new Universe((WorldServer) event.getWorld());
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
		new ForgeUniverseLoadedEvent.Pre(Universe.INSTANCE, data).post();

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
						UUID uuid = StringUtils.fromString(FileUtils.getRawFileName(f));

						if (uuid != null)
						{
							NBTTagCompound nbt = NBTUtils.readTag(f);

							if (nbt != null)
							{
								playerNBT.put(uuid, nbt);
								ForgePlayer player = new ForgePlayer(uuid, nbt.getString("Name"));
								player.firstLogin = false;
								Universe.INSTANCE.players.put(uuid, player);
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
							Universe.INSTANCE.teams.put(s, new ForgeTeam(s, null));
						}
					}
				}
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		for (ForgePlayer player : Universe.INSTANCE.players.values())
		{
			NBTTagCompound nbt = playerNBT.get(player.getId());

			player.hideTeamNotification.setBoolean(nbt.getBoolean("HideTeamNotification"));

			player.setTeamID(nbt.getString("TeamID"));
			player.dataStorage.deserializeNBT(nbt.hasKey("Caps") ? nbt.getCompoundTag("Caps") : nbt.getCompoundTag("Data"));
		}

		Universe.INSTANCE.players.put(ForgePlayerFake.SERVER.getId(), ForgePlayerFake.SERVER);

		for (ForgeTeam team : Universe.INSTANCE.teams.values())
		{
			NBTTagCompound nbt = teamNBT.get(team.getName());

			team.owner = Universe.INSTANCE.getPlayer(StringUtils.fromString(nbt.getString("Owner")));
			team.color.setValueFromString(nbt.getString("Color"), false);
			team.title.setString(nbt.getString("Title"));
			team.desc.setString(nbt.getString("Desc"));

			if (nbt.hasKey("Flags"))
			{
				int flags = nbt.getInteger("Flags");
				team.freeToJoin.setBoolean(Bits.getFlag(flags, 1));
				team.isValid = !Bits.getFlag(flags, 2);
			}
			else
			{
				team.freeToJoin.setBoolean(nbt.getBoolean("FreeToJoin"));
			}

			team.players.clear();

			if (nbt.hasKey("Players"))
			{
				NBTTagCompound nbt1 = nbt.getCompoundTag("Players");

				for (String s : nbt1.getKeySet())
				{
					UUID id = StringUtils.fromString(s);

					if (id != null)
					{
						EnumTeamStatus status = EnumTeamStatus.NAME_MAP.get(nbt1.getString(s));

						if (status.canBeSet())
						{
							team.players.put(id, status);
						}
					}
				}
			}

			NBTTagList list = nbt.getTagList("Invited", Constants.NBT.TAG_STRING);

			for (int i = 0; i < list.tagCount(); i++)
			{
				UUID id = StringUtils.fromString(list.getStringTagAt(i));

				if (id != null && !team.players.containsKey(id))
				{
					team.players.put(id, EnumTeamStatus.INVITED);
				}
			}

			team.dataStorage.deserializeNBT(nbt.getCompoundTag("Data"));
			team.chatHistory.clear();

			if (nbt.hasKey("Chat"))
			{
				NBTTagList list1 = nbt.getTagList("Chat", Constants.NBT.TAG_COMPOUND);

				for (int i = 0; i < list1.tagCount(); i++)
				{
					team.chatHistory.add(new ForgeTeam.Message(list1.getCompoundTagAt(i)));
				}
			}
		}

		new ForgeUniverseLoadedEvent.Post(Universe.INSTANCE, data).post();
		new ForgeUniverseLoadedEvent.Finished(Universe.INSTANCE).post();

		FTBLibAPI.API.loadWorldData(server);
		FTBLibAPI.API.reload(Side.SERVER, server, EnumReloadType.CREATED, ReloadEvent.ALL);
	}

	@SubscribeEvent
	public static void onWorldUnloaded(WorldEvent.Unload event)
	{
		MinecraftServer server = event.getWorld().getMinecraftServer();

		if (event.getWorld().provider.getDimension() != 0 || server == null)
		{
			return;
		}

		Universe.INSTANCE.onClosed();
		Universe.INSTANCE = null;
	}

	@SubscribeEvent
	public static void onWorldSaved(WorldEvent.Save event)
	{
		MinecraftServer server = event.getWorld().getMinecraftServer();

		if (event.getWorld().provider.getDimension() != 0 || server == null)
		{
			return;
		}

		try
		{
			JsonUtils.toJson(new File(CommonUtils.folderWorld, "world_data.json"), SharedServerData.INSTANCE.getSerializableElement());
			File folder = new File(CommonUtils.folderWorld, "data/ftb_lib");

			for (ForgePlayer player : Universe.INSTANCE.players.values())
			{
				if (!player.isFake())
				{
					NBTTagCompound nbt = new NBTTagCompound();

					nbt.setBoolean("HideTeamNotification", player.hideTeamNotification.getBoolean());

					if (player.team != null && player.team.isValid())
					{
						nbt.setString("TeamID", player.team.getName());
					}

					if (!player.dataStorage.isEmpty())
					{
						nbt.setTag("Data", player.dataStorage.serializeNBT());
					}

					nbt.setString("Name", player.getName());

					NBTUtils.writeTag(new File(folder, "players/" + StringUtils.fromUUID(player.getId()) + ".dat"), nbt);
				}
			}

			for (ForgeTeam team : Universe.INSTANCE.teams.values())
			{
				NBTTagCompound nbt = new NBTTagCompound();
				nbt.setString("Owner", StringUtils.fromUUID(team.owner.getId()));
				nbt.setString("Color", team.color.getString());

				if (!team.title.isEmpty())
				{
					nbt.setString("Title", team.title.getString());
				}

				if (!team.desc.isEmpty())
				{
					nbt.setString("Desc", team.desc.getString());
				}

				nbt.setBoolean("FreeToJoin", team.freeToJoin.getBoolean());

				if (team.players != null && !team.players.isEmpty())
				{
					NBTTagCompound nbt1 = new NBTTagCompound();

					for (Map.Entry<UUID, EnumTeamStatus> entry : team.players.entrySet())
					{
						nbt1.setString(StringUtils.fromUUID(entry.getKey()), entry.getValue().getName());
					}

					nbt.setTag("Players", nbt1);
				}

				if (!team.dataStorage.isEmpty())
				{
					nbt.setTag("Data", team.dataStorage.serializeNBT());
				}

				if (team.chatHistory != null && !team.chatHistory.isEmpty())
				{
					NBTTagList list = new NBTTagList();

					for (ITeamMessage msg : team.chatHistory)
					{
						list.appendTag(ForgeTeam.Message.toNBT(msg));
					}

					nbt.setTag("Chat", list);
				}

				NBTUtils.writeTag(new File(folder, "teams/" + team.getName() + ".dat"), nbt);
			}

			NBTTagCompound nbt = new NBTTagCompound();
			NBTTagCompound data = new NBTTagCompound();
			new ForgeUniverseSavedEvent(Universe.INSTANCE, data).post();
			nbt.setTag("Data", data);
			NBTUtils.writeTag(new File(folder, "universe.dat"), nbt);

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
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

		ForgePlayer p = Universe.INSTANCE.getPlayer(player.getGameProfile());
		boolean firstLogin = p == null;

		if (firstLogin)
		{
			p = new ForgePlayer(player.getGameProfile().getId(), player.getGameProfile().getName());
			Universe.INSTANCE.players.put(p.getId(), p);
		}
		else if (!p.getName().equals(player.getName()))
		{
			p.setUsername(player.getGameProfile().getName());
		}

		p.onLoggedIn(player, firstLogin);

		if (firstLogin && (FTBLibConfig.teams.autocreate_teams || !player.mcServer.isDedicatedServer()))
		{
			String id = p.getName().toLowerCase();

			if (Universe.INSTANCE.getTeam(id) != null)
			{
				id = StringUtils.fromUUID(p.getId());
			}

			if (Universe.INSTANCE.getTeam(id) == null)
			{
				ForgeTeam team = new ForgeTeam(id, p);
				Universe.INSTANCE.teams.put(team.getName(), team);
				team.changeOwner(p);
				new ForgeTeamCreatedEvent(team).post();
				new ForgeTeamPlayerJoinedEvent(team, p).post();
			}
		}

		if (!p.hideTeamNotification() && p.getTeam() == null)
		{
			ITextComponent b1 = FTBLibLang.CLICK_HERE.textComponent();
			b1.getStyle().setColor(TextFormatting.GOLD);
			b1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb team gui"));
			b1.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.MY_TEAM.textComponent()));
			ITextComponent b2 = FTBLibLang.CLICK_HERE.textComponent();
			b2.getStyle().setColor(TextFormatting.GOLD);
			b2.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb my_settings ftbl.hide_team_notification toggle"));
			b2.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.TEAM_NOTIFICATION_HIDE.textComponent()));
			player.sendMessage(FTBLibLang.TEAM_NOTIFICATION.textComponent(b1, b2));
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if (event.player instanceof EntityPlayerMP && Universe.INSTANCE != null)
		{
			ForgePlayer p = Universe.INSTANCE.getPlayer(event.player);
			p.loggingOut = true;
			//FTBLibStats.updateLastSeen(stats());
			new ForgePlayerLoggedOutEvent(p).post();
			p.entityPlayer = null;
			p.playerNBT = null;
		}
	}

	@SubscribeEvent
	public static void registerReloadIds(ReloadEvent.RegisterIds event)
	{
		event.register(RELOAD_CONFIG);
	}

	@SubscribeEvent
	public static void onReload(ReloadEvent event)
	{
		if (event.getSide().isServer())
		{
			if (event.reload(RELOAD_CONFIG))
			{
				FTBLibConfig.sync();
			}
		}
	}

    /*
	@SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent e)
    {
    }
    */
}