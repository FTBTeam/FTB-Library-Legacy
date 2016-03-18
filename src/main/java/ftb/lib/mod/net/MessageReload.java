package ftb.lib.mod.net;

import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.events.ReloadEvent;
import ftb.lib.api.net.*;
import ftb.lib.api.notification.*;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.client.*;
import latmod.lib.*;
import latmod.lib.config.ConfigGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageReload extends MessageLM_IO
{
	public MessageReload() { super(ByteCount.INT); }
	
	public MessageReload(ForgeWorldMP w, boolean reloadClient)
	{
		this();
		io.writeBoolean(reloadClient);
		writeSyncedConfig(io);
		NBTTagCompound tag = new NBTTagCompound();
		w.writeDataToNet(tag, null);
		LMNBTUtils.writeTag(io, tag);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		String mode0 = ForgeWorldSP.inst.getMode().getID();
		
		boolean reloadClient = io.readBoolean();
		
		long ms = LMUtils.millis();
		readSyncedConfig(io);
		NBTTagCompound tag = LMNBTUtils.readTag(io);
		ForgeWorldSP.inst.readDataFromNet(tag, false);
		
		if(reloadClient)
		{
			reloadClient(ms, true, !ForgeWorldSP.inst.getMode().getID().equals(mode0));
		}
		else
		{
			Notification n = new Notification("reload_client_config", FTBLibMod.mod.chatComponent("reload_client_config"), 7000);
			n.title.getChatStyle().setColor(EnumChatFormatting.WHITE);
			n.desc = new ChatComponentText("/" + FTBLibModClient.reload_client_cmd.get());
			n.setColor(0xFF333333);
			ClientNotifications.add(n);
			return null;
		}
		
		return null;
	}
	
	@SideOnly(Side.CLIENT)
	public static void reloadClient(long ms, boolean printMessage, boolean modeChanged)
	{
		if(ms == 0L) ms = LMUtils.millis();
		GameModes.reload();
		Shortcuts.load();
		EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
		ReloadEvent event = new ReloadEvent(ForgeWorldSP.inst, ep, true, modeChanged);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onReloaded(event);
		MinecraftForge.EVENT_BUS.post(event);
		
		if(printMessage)
		{
			FTBLib.printChat(ep, new ChatComponentTranslation("ftbl:reloadedClient", ((LMUtils.millis() - ms) + "ms")));
		}
	}
	
	static void writeSyncedConfig(ByteIOStream out)
	{
		try { ConfigRegistry.synced.write(out); }
		catch(Exception ex) {}
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced config TX: " + ConfigRegistry.synced.getJson());
	}
	
	static void readSyncedConfig(ByteIOStream in)
	{
		ConfigGroup synced = new ConfigGroup(ConfigRegistry.synced.getID());
		try { synced.read(in); }
		catch(Exception ex) {}
		ConfigRegistry.synced.loadFromGroup(synced);
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced config RX: " + synced.getJson());
	}
}