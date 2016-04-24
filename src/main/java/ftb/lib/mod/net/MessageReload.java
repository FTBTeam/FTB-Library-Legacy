package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import ftb.lib.FTBLib;
import ftb.lib.FTBWorld;
import ftb.lib.api.EventFTBReload;
import ftb.lib.api.EventFTBSync;
import ftb.lib.api.GameModes;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.api.notification.ClientNotifications;
import ftb.lib.api.notification.Notification;
import ftb.lib.mod.FTBLibLang;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.client.FTBLibModClient;
import latmod.lib.ByteCount;
import latmod.lib.LMUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class MessageReload extends MessageLM
{
	public MessageReload() { super(ByteCount.INT); }
	
	public MessageReload(int reloadClient, NBTTagCompound sync)
	{
		this();
		io.writeByte(reloadClient);
		if(sync == null) sync = new NBTTagCompound();
		sync.setString("Mode", FTBWorld.server.getMode().getID());
		
		if(reloadClient > 0)
		{
			writeTag(sync);
			if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced data TX: " + sync);
		}
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
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
			n.desc = new ChatComponentText('/' + FTBLibModClient.reload_client_cmd.getAsString());
			n.setColor(0xFF333333);
			ClientNotifications.add(n);
			return null;
		}
		
		long ms = LMUtils.millis();
		NBTTagCompound tag = readTag();
		FTBWorld.client.setModeRaw(tag.getString("Mode"));
		reloadClient(ms, reload > 1);
		EventFTBSync.readData(tag, false);
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Synced data RX: " + tag);
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
}