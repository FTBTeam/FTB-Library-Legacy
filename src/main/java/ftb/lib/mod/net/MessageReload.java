package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.*;
import ftb.lib.api.net.*;
import ftb.lib.api.notification.*;
import ftb.lib.mod.*;
import ftb.lib.mod.client.FTBLibModClient;
import latmod.lib.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;

public class MessageReload extends MessageLM
{
	public MessageReload() { super(ByteCount.INT); }
	
	public MessageReload(FTBWorld w, int reloadClient)
	{
		this();
		io.writeByte(reloadClient);
		
		if(reloadClient > 0)
		{
			w.writeReloadData(io);
			writeSyncedConfig(io);
		}
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		if(FTBLib.DEV_ENV)
		{
			FTBLib.dev_logger.info("--------< RELOADING >----------");
		}
		
		byte reload = io.readByte();
		
		if(reload == 0)
		{
			Notification n = new Notification("reload_client_config", FTBLibLang.reload_client_config.chatComponent(), 7000);
			n.title.getChatStyle().setColor(EnumChatFormatting.WHITE);
			n.desc = new ChatComponentText("/" + FTBLibModClient.reload_client_cmd.getAsString());
			n.setColor(0xFF333333);
			ClientNotifications.add(n);
			return null;
		}
		
		long ms = LMUtils.millis();
		FTBWorld.client.readReloadData(io);
		readSyncedConfig(io);
		
		if(reload > 0)
		{
			reloadClient(ms, reload > 1);
		}
		
		return null;
	}
	
	public static void reloadClient(long ms, boolean printMessage)
	{
		if(ms == 0L) ms = LMUtils.millis();
		GameModes.reload();
		EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
		EventFTBReload event = new EventFTBReload(FTBWorld.client, ep, true);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onReloaded(event);
		event.post();
		
		if(printMessage)
		{
			FTBLibLang.reload_client.printChat(ep, (LMUtils.millis() - ms) + "ms");
		}
		
		FTBLibMod.logger.info("Current Mode: " + FTBWorld.client.getMode());
	}
	
	static void writeSyncedConfig(ByteIOStream out)
	{
		NBTTagCompound tag = new NBTTagCompound();
		ConfigRegistry.synced.writeToNBT(tag, false);
		LMNBTUtils.writeTag(out, tag);
		
		if(FTBLib.DEV_ENV)
			FTBLib.dev_logger.info("Synced config TX: " + ConfigRegistry.synced.getSerializableElement());
	}
	
	static void readSyncedConfig(ByteIOStream in)
	{
		NBTTagCompound tag = LMNBTUtils.readTag(in);
		ConfigGroup synced = new ConfigGroup(ConfigRegistry.synced.getID());
		synced.readFromNBT(tag, false);
		ConfigRegistry.synced.loadFromGroup(synced, true);
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced config RX: " + synced.getSerializableElement());
	}
}