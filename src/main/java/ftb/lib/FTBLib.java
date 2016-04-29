package ftb.lib;

import com.google.gson.JsonElement;
import com.mojang.authlib.GameProfile;
import ftb.lib.api.ForgePlayer;
import ftb.lib.api.ForgeWorldMP;
import ftb.lib.api.GameModes;
import ftb.lib.api.ServerTickCallback;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.events.ReloadEvent;
import ftb.lib.api.notification.Notification;
import ftb.lib.api.tile.IGuiTile;
import ftb.lib.mod.FTBLibEventHandler;
import ftb.lib.mod.FTBLibLang;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.FTBUIntegration;
import ftb.lib.mod.net.MessageNotifyPlayer;
import ftb.lib.mod.net.MessageOpenGuiTile;
import ftb.lib.mod.net.MessageReload;
import latmod.lib.LMUtils;
import latmod.lib.net.LMURLConnection;
import latmod.lib.net.RequestMethod;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import net.minecraft.world.WorldEntitySpawner;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.IWorldGenerator;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
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
	
	public static final TextFormatting[] chatColors = new TextFormatting[] {TextFormatting.BLACK, TextFormatting.DARK_BLUE, TextFormatting.DARK_GREEN, TextFormatting.DARK_AQUA, TextFormatting.DARK_RED, TextFormatting.DARK_PURPLE, TextFormatting.GOLD, TextFormatting.GRAY, TextFormatting.DARK_GRAY, TextFormatting.BLUE, TextFormatting.GREEN, TextFormatting.AQUA, TextFormatting.RED, TextFormatting.LIGHT_PURPLE, TextFormatting.YELLOW, TextFormatting.WHITE,};
	
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
		{
			FTBLibLang.reload_server.printChat(BroadcastSender.inst, (LMUtils.millis() - ms) + "ms");
		}
		
		if(hasOnlinePlayers())
		{
			for(EntityPlayerMP ep : getAllOnlinePlayers(null))
			{
				new MessageReload(ForgeWorldMP.inst.getPlayer(ep), false, reloadClient, modeChanged).sendTo(ep);
			}
		}
	}
	
	public static ITextComponent getChatComponent(Object o)
	{ return (o != null && o instanceof ITextComponent) ? (ITextComponent) o : new TextComponentString("" + o); }
	
	public static void addTile(Class<? extends TileEntity> c, ResourceLocation id)
	{ GameRegistry.registerTileEntity(c, id.toString().replace(':', '.')); }
	
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
	
	public static boolean hasOnlinePlayers()
	{ return getServer() != null && !getServer().getPlayerList().getPlayerList().isEmpty(); }
	
	public static List<EntityPlayerMP> getAllOnlinePlayers(EntityPlayerMP except)
	{
		if(getServer() == null) return new ArrayList<>();
		else if(except == null) return getServer().getPlayerList().getPlayerList();
		
		List<EntityPlayerMP> l = getServer().getPlayerList().getPlayerList();
		if(l.isEmpty()) return l;
		
		List<EntityPlayerMP> list = new ArrayList<>();
		list.addAll(l);
		if(except != null) list.remove(except);
		return list;
	}
	
	public static EntityPlayerMP getPlayerMP(UUID id)
	{
		if(!hasOnlinePlayers()) return null;
		return getServer().getPlayerList().getPlayerByUUID(id);
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
		return !isDedicatedServer() || getServerWorld() != null && getServer().getPlayerList().getOppedPlayers().getPermissionLevel(p) > 0;
	}
	
	public static void notifyPlayer(EntityPlayerMP ep, Notification n)
	{ new MessageNotifyPlayer(n).sendTo(ep); }
	
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
	
	//null - can't, TRUE - always spawns, FALSE - only spawns at night
	public static Boolean canMobSpawn(World world, BlockPos pos)
	{
		if(world == null || pos == null || pos.getY() < 0 || pos.getY() >= 256) return null;
		Chunk chunk = world.getChunkFromBlockCoords(pos);
		
		if(!WorldEntitySpawner.canCreatureTypeSpawnAtLocation(EntityLiving.SpawnPlacementType.ON_GROUND, world, pos) || chunk.getLightFor(EnumSkyBlock.BLOCK, pos) >= 8)
			return null;
		
		AxisAlignedBB aabb = new AxisAlignedBB(pos.getX() + 0.2, pos.getY() + 0.01, pos.getZ() + 0.2, pos.getX() + 0.8, pos.getY() + 1.8, pos.getZ() + 0.8);
		if(!world.checkNoEntityCollision(aabb) || world.isAnyLiquid(aabb)) return null;
		
		if(chunk.getLightFor(EnumSkyBlock.SKY, pos) >= 8) return Boolean.FALSE;
		return Boolean.TRUE;
	}
	
	public static Entity getEntityByUUID(World worldObj, UUID uuid)
	{
		if(worldObj == null || uuid == null) return null;
		
		for(Entity e : worldObj.loadedEntityList)
		{
			if(e.getUniqueID().equals(uuid))
			{
				return e;
			}
		}
		
		return null;
	}
}