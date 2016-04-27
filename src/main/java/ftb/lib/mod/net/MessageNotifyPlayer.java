package ftb.lib.mod.net;

import com.google.gson.JsonElement;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.EnumScreen;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.api.notification.ClientNotifications;
import ftb.lib.api.notification.Notification;
import ftb.lib.mod.client.FTBLibModClient;
import io.netty.buffer.ByteBuf;

public class MessageNotifyPlayer extends MessageLM<MessageNotifyPlayer>
{
	public JsonElement json;
	
	public MessageNotifyPlayer() { }
	
	public MessageNotifyPlayer(Notification n)
	{
		json = n.getSerializableElement();
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		json = readJsonElement(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeJsonElement(io, json);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageNotifyPlayer m, MessageContext ctx)
	{
		if(FTBLibModClient.notifications.get() != EnumScreen.OFF)
		{
			Notification n = Notification.deserialize(m.json);
			
			if(FTBLibModClient.notifications.get() == EnumScreen.SCREEN) ClientNotifications.add(n);
			else
			{
				FTBLibClient.mc.thePlayer.addChatMessage(n.title);
				if(n.desc != null) FTBLibClient.mc.thePlayer.addChatMessage(n.desc);
			}
		}
		
		return null;
	}
}