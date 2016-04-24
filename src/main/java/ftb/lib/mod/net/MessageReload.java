package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.FTBLib;
import ftb.lib.FTBWorld;
import ftb.lib.ReloadType;
import ftb.lib.api.EventFTBReload;
import ftb.lib.api.EventFTBSync;
import ftb.lib.api.EventFTBWorldClient;
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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class MessageReload extends MessageLM
{
	public MessageReload() { super(ByteCount.INT); }
	
	public MessageReload(ReloadType t, EntityPlayerMP ep, boolean login)
	{
		this();
		io.writeByte(t.ordinal());
		io.writeBoolean(login);
		io.writeUTF(FTBWorld.server.getMode().getID());
		writeTag(EventFTBSync.generateData(ep, login));
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		long ms = LMUtils.millis();
		
		ReloadType type = ReloadType.values()[io.readUnsignedByte()];
		boolean login = io.readBoolean();
		String mode = io.readUTF();
		
		boolean first = FTBWorld.client == null;
		if(first) FTBWorld.client = new FTBWorld(Side.CLIENT);
		
		FTBWorld.client.setModeRaw(mode);
		EventFTBSync.readData(readTag(), false);
		
		new EventFTBWorldClient(FTBWorld.client).post();
		
		if(type.reload(Side.CLIENT))
		{
			reloadClient(ms, type, login);
		}
		else if(type == ReloadType.SERVER_ONLY_NOTIFY_CLIENT)
		{
			Notification n = new Notification("reload_client_config", FTBLibLang.reload_client_config.chatComponent(), 7000);
			n.title.getChatStyle().setColor(EnumChatFormatting.WHITE);
			n.desc = new ChatComponentText('/' + FTBLibModClient.reload_client_cmd.getAsString());
			n.setColor(0xFF333333);
			ClientNotifications.add(n);
		}
		
		return null;
	}
	
	public static void reloadClient(long ms, ReloadType type, boolean login)
	{
		if(ms == 0L) ms = LMUtils.millis();
		GameModes.reload();
		EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
		EventFTBReload event = new EventFTBReload(FTBWorld.client, ep, type, login);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onReloaded(event);
		event.post();
		
		if(!login)
		{
			FTBLibLang.reload_client.printChat(ep, (LMUtils.millis() - ms) + "ms");
		}
		
		FTBLibMod.logger.info("Current Mode: " + FTBWorld.client.getMode());
	}
}