package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.FTBLib;
import ftb.lib.FTBWorld;
import ftb.lib.LMNBTUtils;
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
import io.netty.buffer.ByteBuf;
import latmod.lib.LMMapUtils;
import latmod.lib.LMUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.Map;

public class MessageReload extends MessageLM<MessageReload>
{
	public int typeID;
	public boolean login;
	public String modeID;
	public NBTTagCompound tag;
	
	public MessageReload() { }
	
	public MessageReload(ReloadType t, EntityPlayerMP ep, boolean l)
	{
		typeID = t.ordinal();
		login = l;
		modeID = FTBWorld.server.getMode().getID();
		tag = EventFTBSync.generateData(ep, login);
		
		System.out.println("NBT TREE: " + LMMapUtils.toString((Map<String, Object>) LMNBTUtils.generateTree(tag)));
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		typeID = io.readUnsignedByte();
		login = io.readBoolean();
		modeID = readString(io);
		tag = readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeByte(typeID);
		io.writeBoolean(login);
		writeString(io, modeID);
		writeTag(io, tag);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageReload m, MessageContext ctx)
	{
		long ms = LMUtils.millis();
		
		ReloadType type = ReloadType.values()[m.typeID];
		
		boolean first = FTBWorld.client == null;
		if(first) FTBWorld.client = new FTBWorld(Side.CLIENT);
		
		FTBWorld.client.setModeRaw(m.modeID);
		EventFTBSync.readData(m.tag, false);
		
		new EventFTBWorldClient(FTBWorld.client).post();
		
		if(type.reload(Side.CLIENT))
		{
			reloadClient(ms, type, m.login);
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