package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.EnumScreen;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.*;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.notification.*;
import latmod.lib.ByteCount;

public class MessageNotifyPlayer extends MessageLM
{
	public MessageNotifyPlayer() { super(ByteCount.SHORT); }
	
	public MessageNotifyPlayer(Notification n)
	{
		this();
		io.writeUTF(n.toJson());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		if(FTBLibModClient.notifications.get() != EnumScreen.OFF)
		{
			Notification n = Notification.fromJson(io.readUTF());
			
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