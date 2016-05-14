package ftb.lib.mod.net;

import ftb.lib.api.ForgePlayerMP;
import ftb.lib.api.ForgeWorldMP;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageToServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageRequestSelfUpdate extends MessageToServer<MessageRequestSelfUpdate>
{
	public MessageRequestSelfUpdate() { }
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
	}
	
	@Override
	public void onMessage(MessageRequestSelfUpdate m, EntityPlayerMP ep)
	{
		ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(ep);
		new MessageLMPlayerUpdate(p, true).sendTo(ep);
	}
}