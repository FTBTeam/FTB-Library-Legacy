package ftb.lib.mod.net;

import ftb.lib.EnumScreen;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.*;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.notification.*;
import latmod.lib.ByteCount;
import latmod.lib.json.JsonElementIO;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageNotifyPlayer extends MessageLM
{
	public MessageNotifyPlayer() { super(ByteCount.SHORT); }
	
	public MessageNotifyPlayer(Notification n)
	{
		this();
		JsonElementIO.write(io, n.getJson());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		if(FTBLibModClient.notifications.get() != EnumScreen.OFF)
		{
			Notification n = Notification.deserialize(JsonElementIO.read(io));
			
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