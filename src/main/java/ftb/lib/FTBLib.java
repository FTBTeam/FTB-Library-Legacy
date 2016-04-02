package ftb.lib;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.events.ReloadEvent;
import ftb.lib.api.item.IItemLM;
import ftb.lib.api.notification.Notification;
import ftb.lib.api.tile.IGuiTile;
import ftb.lib.mod.*;
import ftb.lib.mod.net.*;
import latmod.lib.LMUtils;
import latmod.lib.net.*;
import latmod.lib.util.StringMapEntry;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.registry.*;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.*;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

public class FTBLib
{
	public static final boolean DEV_ENV = (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
	public static boolean userIsLatvianModder = false;
	public static final Logger dev_logger = LogManager.getLogger("FTBLibDev");
	public static final String FORMATTING = "\u00a7";
	public static final Pattern textFormattingPattern = Pattern.compile("(?i)" + FORMATTING + "[0-9A-FK-OR]");
	private static final HashMap<String, UUID> cachedUUIDs = new HashMap<>();
	public static FTBUIntegration ftbu = null;
	
	public static final EnumChatFormatting[] chatColors = new EnumChatFormatting[] {EnumChatFormatting.BLACK, EnumChatFormatting.DARK_BLUE, EnumChatFormatting.DARK_GREEN, EnumChatFormatting.DARK_AQUA, EnumChatFormatting.DARK_RED, EnumChatFormatting.DARK_PURPLE, EnumChatFormatting.GOLD, EnumChatFormatting.GRAY, EnumChatFormatting.DARK_GRAY, EnumChatFormatting.BLUE, EnumChatFormatting.GREEN, EnumChatFormatting.AQUA, EnumChatFormatting.RED, EnumChatFormatting.LIGHT_PURPLE, EnumChatFormatting.YELLOW, EnumChatFormatting.WHITE,};
	
	public static File folderConfig;
	public static File folderMinecraft;
	public static File folderModpack;
	public static File folderLocal;
	
	public static void init(File configFolder)
	{
		folderConfig = configFolder;
		folderMinecraft = folderConfig.getParentFile();
		folderModpack = new File(folderMinecraft, "modpack/");
		folderLocal = new File(folderMinecraft, "local/");
		
		if(!folderModpack.exists()) folderModpack.mkdirs();
		if(!folderLocal.exists()) folderLocal.mkdirs();
		
		if(dev_logger instanceof org.apache.logging.log4j.core.Logger)
		{
			if(DEV_ENV)
				((org.apache.logging.log4j.core.Logger) dev_logger).setLevel(org.apache.logging.log4j.Level.ALL);
			else ((org.apache.logging.log4j.core.Logger) dev_logger).setLevel(org.apache.logging.log4j.Level.OFF);
		}
		else
			FTBLibMod.logger.info("DevLogger isn't org.apache.logging.log4j.core.Logger! It's " + dev_logger.getClass().getName());
	}
	
	public static void reload(ICommandSender sender, boolean printMessage, boolean reloadClient)
	{
		if(ForgeWorldMP.inst == null) return;
		String mode0 = ForgeWorldMP.inst.getMode().getID();
		
		long ms = LMUtils.millis();
		ConfigRegistry.reload();
		GameModes.reload();
		
		for(ForgePlayer p : ForgeWorldMP.inst.playerMap.values())
			p.toPlayerMP().stats.refresh(p.toPlayerMP(), false);
		
		boolean modeChanged = !ForgeWorldMP.inst.getMode().getID().equals(mode0);
		
		ReloadEvent event = new ReloadEvent(ForgeWorldMP.inst, sender, reloadClient, modeChanged);
		if(ftbu != null) ftbu.onReloaded(event);
		MinecraftForge.EVENT_BUS.post(event);
		
		if(printMessage)
			printChat(BroadcastSender.inst, FTBLibMod.mod.chatComponent("reloaded_server", ((LMUtils.millis() - ms) + "ms")));
		
		if(hasOnlinePlayers())
		{
			for(EntityPlayerMP ep : getAllOnlinePlayers(null))
			{
				new MessageReload(ForgeWorldMP.inst.getPlayer(ep), false, reloadClient, modeChanged).sendTo(ep);
			}
		}
	}
	
	public static IChatComponent getChatComponent(Object o)
	{ return (o != null && o instanceof IChatComponent) ? (IChatComponent) o : new ChatComponentText("" + o); }
	
	public static void addItem(IItemLM i)
	{ addItem((Item) i, i.getID()); }
	
	public static void addItem(Item i, String name)
	{ GameRegistry.registerItem(i, name); }
	
	public static void addBlock(Block b, Class<? extends ItemBlock> c, String name)
	{ GameRegistry.registerBlock(b, c, name); }
	
	public static void addBlock(Block b, String name)
	{ addBlock(b, ItemBlock.class, name); }
	
	public static void addTileEntity(Class<? extends TileEntity> c, String s, String... alt)
	{
		if(alt == null || alt.length == 0) GameRegistry.registerTileEntity(c, s);
		else GameRegistry.registerTileEntityWithAlternatives(c, s, alt);
	}
	
	public static void addEntity(Class<? extends Entity> c, String s, int id, Object mod)
	{ EntityRegistry.registerModEntity(c, s, id, mod, 50, 1, true); }
	
	public static void addWorldGenerator(IWorldGenerator i, int w)
	{ GameRegistry.registerWorldGenerator(i, w); }
	
	public static Fluid addFluid(Fluid f)
	{
		Fluid f1 = FluidRegistry.getFluid(f.getName());
		if(f1 != null) return f1;
		FluidRegistry.registerFluid(f);
		return f;
	}
	
	public static void addCommand(FMLServerStartingEvent e, ICommand c)
	{ if(c != null && !c.getCommandName().isEmpty()) e.registerServerCommand(c); }
	
	/**
	 * Prints message to chat (doesn't translate it)
	 */
	public static void printChat(ICommandSender ep, Object o)
	{
		if(ep == null) ep = FTBLibMod.proxy.getClientPlayer();
		if(ep != null) ep.addChatMessage(getChatComponent(o));
		else FTBLibMod.logger.info(o);
	}
	
	public static MinecraftServer getServer()
	{ return FMLCommonHandler.instance().getMinecraftServerInstance(); }
	
	public static Side getEffectiveSide()
	{ return FMLCommonHandler.instance().getEffectiveSide(); }
	
	public static boolean isDedicatedServer()
	{
		MinecraftServer mcs = getServer();
		return mcs != null && mcs.isDedicatedServer();
	}
	
	public static String getPath(ResourceLocation res)
	{ return "/assets/" + res.getResourceDomain() + "/" + res.getResourcePath(); }
	
	public static boolean resourceExists(ResourceLocation res)
	{ return FTBLib.class.getResource(getPath(res)) != null; }
	
	public static boolean hasOnlinePlayers()
	{ return getServer() != null && !getServer().getConfigurationManager().playerEntityList.isEmpty(); }
	
	public static List<EntityPlayerMP> getAllOnlinePlayers(EntityPlayerMP except)
	{
		if(getServer() == null) return new ArrayList<>();
		else if(except == null) return getServer().getConfigurationManager().playerEntityList;
		
		List<EntityPlayerMP> l = getServer().getConfigurationManager().playerEntityList;
		if(l.isEmpty()) return l;
		
		List<EntityPlayerMP> list = new ArrayList<>();
		list.addAll(l);
		if(except != null) list.remove(except);
		return list;
	}
	
	public static Map<UUID, EntityPlayerMP> getAllOnlinePlayersMap()
	{
		HashMap<UUID, EntityPlayerMP> m = new HashMap<>();
		
		if(!hasOnlinePlayers()) return m;
		
		for(int i = 0; i < getServer().getConfigurationManager().playerEntityList.size(); i++)
		{
			EntityPlayerMP ep = getServer().getConfigurationManager().playerEntityList.get(i);
			m.put(ep.getUniqueID(), ep);
		}
		
		return m;
	}
	
	public static EntityPlayerMP getPlayerMP(UUID id)
	{
		if(!hasOnlinePlayers()) return null;
		
		for(int i = 0; i < getServer().getConfigurationManager().playerEntityList.size(); i++)
		{
			EntityPlayerMP ep = getServer().getConfigurationManager().playerEntityList.get(i);
			if(ep.getUniqueID().equals(id)) return ep;
		}
		
		return null;
	}
	
	public static boolean remap(FMLMissingMappingsEvent.MissingMapping m, String id, Item i)
	{
		if(m.type == GameRegistry.Type.ITEM && id.equals(m.name))
		{
			m.remap(i);
			return true;
		}
		
		return false;
	}
	
	public static boolean remap(FMLMissingMappingsEvent.MissingMapping m, String id, Block b)
	{
		if(id.equals(m.name))
		{
			if(m.type == GameRegistry.Type.BLOCK) m.remap(b);
			else if(m.type == GameRegistry.Type.ITEM) m.remap(Item.getItemFromBlock(b));
			return true;
		}
		
		return false;
	}
	
	public static boolean isModInstalled(String s)
	{ return Loader.isModLoaded(s); }
	
	public static String removeFormatting(String s)
	{
		if(s == null) return null;
		if(s.isEmpty()) return "";
		return textFormattingPattern.matcher(s).replaceAll("");
	}
	
	public static WorldServer getServerWorld()
	{
		MinecraftServer ms = getServer();
		if(ms == null || ms.worldServers.length < 1) return null;
		return ms.worldServers[0];
	}
	
	public static int runCommand(ICommandSender ics, String s) throws CommandException
	{ return getServer().getCommandManager().executeCommand(ics, s); }
	
	public static int runCommand(ICommandSender ics, String cmd, String[] args) throws CommandException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(cmd);
		if(args != null && args.length > 0)
		{
			for(String arg : args)
			{
				sb.append(' ');
				sb.append(arg);
			}
		}
		
		return runCommand(ics, sb.toString());
	}
	
	public static void openGui(EntityPlayer ep, IGuiTile t, NBTTagCompound data)
	{
		if(t == null || !(t instanceof TileEntity) || ep instanceof FakePlayer) return;
		else if(ep instanceof EntityPlayerMP)
		{
			Container c = t.getContainer(ep, data);
			if(c == null) return;
			
			EntityPlayerMP epM = (EntityPlayerMP) ep;
			epM.getNextWindowId();
			epM.closeContainer();
			epM.openContainer = c;
			epM.openContainer.windowId = epM.currentWindowId;
			epM.openContainer.onCraftGuiOpened(epM);
			new MessageOpenGuiTile((TileEntity) t, data, epM.currentWindowId).sendTo(epM);
		}
		else if(!getEffectiveSide().isServer())
			FTBLibMod.proxy.openClientTileGui((ep == null) ? FTBLibMod.proxy.getClientPlayer() : ep, t, data);
	}
	
	public static void addCallback(ServerTickCallback c)
	{
		if(c.maxTick == 0) c.onCallback();
		else FTBLibEventHandler.pendingCallbacks.add(c);
	}
	
	public static boolean isOP(GameProfile p)
	{
		return !isDedicatedServer() || getServerWorld() != null && getServer().getConfigurationManager().getOppedPlayers().getEntry(p) != null;
	}
	
	public static void notifyPlayer(EntityPlayerMP ep, Notification n)
	{ new MessageNotifyPlayer(n).sendTo(ep); }
	
	@SuppressWarnings("all")
	public static List<ICommand> getAllCommands(ICommandSender sender)
	{
		ArrayList<ICommand> commands = new ArrayList<>();
		ArrayList<String> cmdIDs = new ArrayList<>();
		
		for(Object o : getServer().getCommandManager().getPossibleCommands(sender))
		{
			ICommand c = (ICommand) o;
			if(!cmdIDs.contains(c.getCommandName()))
			{
				commands.add(c);
				cmdIDs.add(c.getCommandName());
			}
		}
		
		return commands;
	}
	
	public static UUID getPlayerID(String s)
	{
		if(s == null || s.isEmpty()) return null;
		
		String key = s.trim().toLowerCase();
		
		if(!cachedUUIDs.containsKey(key))
		{
			cachedUUIDs.put(key, null);
			
			try
			{
				JsonElement e = new LMURLConnection(RequestMethod.GET, "https://api.mojang.com/users/profiles/minecraft/" + s).connect().asJson();
				cachedUUIDs.put(key, LMUtils.fromString(e.getAsJsonObject().get("id").getAsString()));
			}
			catch(Exception e) { }
		}
		
		return cachedUUIDs.get(key);
	}
	
	public static void playSoundEffect(World w, BlockPos pos, String s, float g, float p)
	{ w.playSoundEffect(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, s, g, p); }
	
	//null - can't, TRUE - always spawns, FALSE - only spawns at night
	public static Boolean canMobSpawn(World world, BlockPos pos)
	{
		if(world == null || pos == null || pos.getY() < 0 || pos.getY() >= 256) return null;
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		
		if(!SpawnerAnimals.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, pos) || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) >= 8)
			return null;
		
		AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() + 0.2, pos.getY() + 0.01, pos.getZ() + 0.2, pos.getX() + 0.8, pos.getY() + 1.8, pos.getZ() + 0.8);
		if(!world.checkNoEntityCollision(aabb) || world.isAnyLiquid(aabb)) return null;
		
		if(chunk.getLightFor(EnumSkyBlock.SKY, pos) >= 8) return Boolean.FALSE;
		return Boolean.TRUE;
	}
	
	public static String getStateString(IBlockState state)
	{
		if(state == null) return null;
		List<StringMapEntry> list = new ArrayList<>();
		
		for(IProperty p : state.getPropertyNames())
		{
			list.add(new StringMapEntry(p.getName(), state.getValue(p)));
		}
		
		Collections.sort(list);
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < list.size(); i++)
		{
			StringMapEntry e = list.get(i);
			sb.append(e.getKey());
			sb.append('=');
			sb.append(e.getValue());
			if(i != list.size() - 1)
			{
				sb.append(',');
			}
		}
		
		return sb.toString();
	}
	
	public static Entity getEntityByUUID(World w, UUID id)
	{
		if(w == null || id == null) return null;
		
		for(Entity e : w.loadedEntityList)
		{
			if(e.getUniqueID().equals(id))
			{
				return e;
			}
		}
		
		return null;
	}
}