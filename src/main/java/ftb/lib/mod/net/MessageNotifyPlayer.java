package ftb.lib.mod.net;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.EnumScreen;
import ftb.lib.api.*;
import ftb.lib.client.FTBLibClient;
import ftb.lib.mod.client.FTBLibModClient;
import ftb.lib.notification.*;

public class MessageNotifyPlayer extends MessageLM
{
	public MessageNotifyPlayer() { super(DATA_SHORT); }
	
	public MessageNotifyPlayer(Notification n)
	{
		this();
		io.writeString(n.toJson());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_GUI; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		if(FTBLibModClient.notifications.get() != EnumScreen.OFF)
		{
			Notification n = Notification.fromJson(io.readString());
			
			if(FTBLibModClient.notifications.get() == EnumScreen.SCREEN)
				ClientNotifications.add(n);
			else
			{
				FTBLibClient.mc.thePlayer.addChatMessage(n.title);
				if(n.desc != null) FTBLibClient.mc.thePlayer.addChatMessage(n.desc);
			}
		}
		
		return null;
	}
}