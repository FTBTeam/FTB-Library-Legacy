package ftb.lib.mod.net;

import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.notification.*;
import latmod.lib.ByteCount;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

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