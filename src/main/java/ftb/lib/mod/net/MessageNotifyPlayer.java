package ftb.lib.mod.net;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.*;
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
		//TODO: Add config
		ClientNotifications.add(Notification.fromJson(io.readString()));
		return null;
	}
}