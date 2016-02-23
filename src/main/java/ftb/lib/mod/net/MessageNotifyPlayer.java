package ftb.lib.mod.net;

import ftb.lib.EnumScreen;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.*;
import ftb.lib.api.notification.*;
import ftb.lib.mod.client.FTBLibModClient;
import io.netty.buffer.ByteBuf;
import latmod.lib.LMJsonUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageNotifyPlayer extends MessageLM<MessageNotifyPlayer>
{
	public String json;
	
	public MessageNotifyPlayer() { }
	
	public MessageNotifyPlayer(Notification n)
	{
		json = LMJsonUtils.toJson(n.getJson());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	public void fromBytes(ByteBuf io)
	{
		json = readString(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		writeString(io, json);
	}
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		if(FTBLibModClient.notifications.get() != EnumScreen.OFF)
		{
			Notification n = Notification.deserialize(LMJsonUtils.fromJson(json));
			
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