package com.feed_the_beast.ftbl;

import com.feed_the_beast.ftbl.api.EnumReloadType;
import com.feed_the_beast.ftbl.api.EnumTeamStatus;
import com.feed_the_beast.ftbl.api.EventHandler;
import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.api.IForgePlayer;
import com.feed_the_beast.ftbl.api.RegisterConfigValueProvidersEvent;
import com.feed_the_beast.ftbl.api.RegisterOptionalServerModsEvent;
import com.feed_the_beast.ftbl.api.ServerReloadEvent;
import com.feed_the_beast.ftbl.api.player.ForgePlayerLoggedOutEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamCreatedEvent;
import com.feed_the_beast.ftbl.api.team.ForgeTeamPlayerJoinedEvent;
import com.feed_the_beast.ftbl.api.team.RegisterTeamGuiActionsEvent;
import com.feed_the_beast.ftbl.api.universe.ForgeUniverseLoadedEvent;
import com.feed_the_beast.ftbl.api.universe.ForgeUniverseSavedEvent;
import com.feed_the_beast.ftbl.api_impl.FTBLibTeamGuiActions;
import com.feed_the_beast.ftbl.api_impl.ForgePlayer;
import com.feed_the_beast.ftbl.api_impl.ForgePlayerFake;
import com.feed_the_beast.ftbl.api_impl.ForgeTeam;
import com.feed_the_beast.ftbl.api_impl.SharedServerData;
import com.feed_the_beast.ftbl.api_impl.Universe;
import com.feed_the_beast.ftbl.lib.config.ConfigBlockState;
import com.feed_the_beast.ftbl.lib.config.ConfigBoolean;
import com.feed_the_beast.ftbl.lib.config.ConfigColor;
import com.feed_the_beast.ftbl.lib.config.ConfigDouble;
import com.feed_the_beast.ftbl.lib.config.ConfigEnum;
import com.feed_the_beast.ftbl.lib.config.ConfigInt;
import com.feed_the_beast.ftbl.lib.config.ConfigItemStack;
import com.feed_the_beast.ftbl.lib.config.ConfigList;
import com.feed_the_beast.ftbl.lib.config.ConfigNull;
import com.feed_the_beast.ftbl.lib.config.ConfigString;
import com.feed_the_beast.ftbl.lib.config.ConfigStringEnum;
import com.feed_the_beast.ftbl.lib.config.ConfigTextComponent;
import com.feed_the_beast.ftbl.lib.config.ConfigTristate;
import com.feed_the_beast.ftbl.lib.internal.FTBLibFinals;
import com.feed_the_beast.ftbl.lib.internal.FTBLibLang;
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
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
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
	public static void registerOptionalServerMods(RegisterOptionalServerModsEvent event)
	{
		event.register(FTBLibFinals.MOD_ID);
	}

	@SubscribeEvent
	public static void registerConfigValueProviders(RegisterConfigValueProvidersEvent event)
	{
		event.register(ConfigNull.ID, () -> ConfigNull.INSTANCE);
		event.register(ConfigList.ID, () -> new ConfigList(ConfigNull.ID));
		event.register(ConfigBoolean.ID, ConfigBoolean::new);
		event.register(ConfigTristate.ID, ConfigTristate::new);
		event.register(ConfigInt.ID, ConfigInt::new);
		event.register(ConfigDouble.ID, ConfigDouble::new);
		event.register(ConfigString.ID, ConfigString::new);
		event.register(ConfigColor.ID, ConfigColor::new);
		event.register(ConfigEnum.ID, ConfigStringEnum::new);
		event.register(ConfigBlockState.ID, ConfigBlockState::new);
		event.register(ConfigItemStack.ID, ConfigItemStack::new);
		event.register(ConfigTextComponent.ID, ConfigTextComponent::new);
	}

	@SubscribeEvent
	public static void registerTeamGuiActions(RegisterTeamGuiActionsEvent event)
	{
		event.register(FTBLibTeamGuiActions.CONFIG);
		event.register(FTBLibTeamGuiActions.INFO);
		event.register(FTBLibTeamGuiActions.MEMBERS);
		event.register(FTBLibTeamGuiActions.ALLIES);
		event.register(FTBLibTeamGuiActions.MODERATORS);
		event.register(FTBLibTeamGuiActions.ENEMIES);
		event.register(FTBLibTeamGuiActions.LEAVE);
		event.register(FTBLibTeamGuiActions.TRANSFER_OWNERSHIP);
	}

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
							Universe.INSTANCE.teams.put(s, new ForgeTeam(s));
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
			player.lastTimeSeen = nbt.getLong("LastTimeSeen");
			player.setTeamId(nbt.getString("TeamID"));
			player.dataStorage.deserializeNBT(nbt.getCompoundTag("Data"));
		}

		Universe.INSTANCE.players.put(ForgePlayerFake.SERVER.getId(), ForgePlayerFake.SERVER);

		Iterator<ForgeTeam> teamIterator = Universe.INSTANCE.teams.values().iterator();

		while (teamIterator.hasNext())
		{
			ForgeTeam team = teamIterator.next();
			NBTTagCompound nbt = teamNBT.get(team.getName());

			team.owner = Universe.INSTANCE.getPlayer(nbt.getString("Owner"));

			if (team.owner == null)
			{
				teamIterator.remove();
				continue;
			}

			team.color.setValueFromString(nbt.getString("Color"), false);
			team.title.setString(nbt.getString("Title"));
			team.desc.setString(nbt.getString("Desc"));
			team.freeToJoin.setBoolean(nbt.getBoolean("FreeToJoin"));

			team.players.clear();

			if (nbt.hasKey("Players"))
			{
				NBTTagCompound nbt1 = nbt.getCompoundTag("Players");

				for (String s : nbt1.getKeySet())
				{
					IForgePlayer player = Universe.INSTANCE.getPlayer(s);

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
				IForgePlayer player = Universe.INSTANCE.getPlayer(list.getStringTagAt(i));

				if (player != null && !team.isMember(player))
				{
					team.setRequestingInvite(player, true);
				}
			}

			list = nbt.getTagList("Invited", Constants.NBT.TAG_STRING);

			for (int i = 0; i < list.tagCount(); i++)
			{
				IForgePlayer player = Universe.INSTANCE.getPlayer(list.getStringTagAt(i));

				if (player != null && !team.isMember(player))
				{
					team.setStatus(player, EnumTeamStatus.INVITED);
				}
			}

			team.dataStorage.deserializeNBT(nbt.getCompoundTag("Data"));
		}

		new ForgeUniverseLoadedEvent.Post(Universe.INSTANCE, data).post();
		new ForgeUniverseLoadedEvent.Finished(Universe.INSTANCE).post();

		FTBLibAPI.API.reloadServer(server, EnumReloadType.CREATED, ServerReloadEvent.ALL);
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

			NBTTagCompound mainNbt = new NBTTagCompound();
			NBTTagCompound data = new NBTTagCompound();
			new ForgeUniverseSavedEvent(Universe.INSTANCE, data).post();
			mainNbt.setTag("Data", data);
			NBTUtils.writeTag(new File(folder, "universe.dat"), mainNbt);

			for (ForgePlayer player : Universe.INSTANCE.players.values())
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

			for (ForgeTeam team : Universe.INSTANCE.teams.values())
			{
				NBTTagCompound nbt = new NBTTagCompound();

				nbt.setString("Owner", team.owner.getName());
				nbt.setString("Color", team.color.getString());
				nbt.setString("Title", team.title.getString());
				nbt.setString("Desc", team.desc.getString());
				nbt.setBoolean("FreeToJoin", team.freeToJoin.getBoolean());

				NBTTagCompound nbt1 = new NBTTagCompound();

				if (!team.players.isEmpty())
				{
					for (Map.Entry<IForgePlayer, EnumTeamStatus> entry : team.players.entrySet())
					{
						nbt1.setString(entry.getKey().getName(), entry.getValue().getName());
					}
				}

				nbt.setTag("Players", nbt1);

				NBTTagList list = new NBTTagList();

				for (IForgePlayer player : team.requestingInvite)
				{
					list.appendTag(new NBTTagString(player.getName()));
				}

				nbt.setTag("RequestingInvite", list);

				nbt.setTag("Data", team.dataStorage.serializeNBT());

				NBTUtils.writeTag(new File(folder, "teams/" + team.getName() + ".dat"), nbt);
			}

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
			p = new ForgePlayer(player.getUniqueID(), player.getName());
			Universe.INSTANCE.players.put(p.getId(), p);
		}
		else if (!p.getName().equals(player.getName()))
		{
			p.setUsername(player.getName());
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
				ForgeTeam team = new ForgeTeam(id);
				Universe.INSTANCE.teams.put(team.getName(), team);
				p.setTeamId(team.getName());
				team.setStatus(p, EnumTeamStatus.OWNER);
				new ForgeTeamCreatedEvent(team).post();
				new ForgeTeamPlayerJoinedEvent(team, p).post();
			}
		}

		if (!p.hideTeamNotification() && p.getTeam() == null)
		{
			ITextComponent b1 = FTBLibLang.CLICK_HERE.textComponent();
			b1.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb team gui"));
			b1.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.MY_TEAM.textComponent()));
			ITextComponent b2 = FTBLibLang.CLICK_HERE.textComponent();
			b2.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ftb my_settings " + FTBLibFinals.MOD_ID + ".hide_team_notification toggle"));
			b2.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, FTBLibLang.TEAM_NOTIFICATION_HIDE.textComponent()));
			player.sendMessage(FTBLibLang.TEAM_NOTIFICATION.textComponent(b1, b2));
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event)
	{
		if (event.player instanceof EntityPlayerMP && FTBLibAPI.API.hasUniverse())
		{
			ForgePlayer player = Universe.INSTANCE.getPlayer(event.player.getGameProfile());

			if (player != null)
			{
				player.lastTimeSeen = event.player.world.getTotalWorldTime();
				player.loggingOut = true;
				//FTBLibStats.updateLastSeen(stats());
				new ForgePlayerLoggedOutEvent(player).post();
				player.entityPlayer = null;
				player.playerNBT = null;
			}
		}
	}

	@SubscribeEvent
	public static void registerReloadIds(ServerReloadEvent.RegisterIds event)
	{
		event.register(RELOAD_CONFIG);
	}

	@SubscribeEvent
	public static void onServerReload(ServerReloadEvent event)
	{
		if (event.reload(RELOAD_CONFIG))
		{
			FTBLibConfig.sync();
		}
	}

    /*
	@SubscribeEvent
    public static void onPlayerInteract(PlayerInteractEvent e)
    {
    }
    */
}