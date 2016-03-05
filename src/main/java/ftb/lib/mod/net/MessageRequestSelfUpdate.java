package ftb.lib.mod.net;

import ftb.lib.api.net.*;
import ftb.lib.api.players.*;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageRequestSelfUpdate extends MessageLM<MessageRequestSelfUpdate>
{
	public MessageRequestSelfUpdate() { }
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public void fromBytes(ByteBuf io)
	{
	}
	
	public void toBytes(ByteBuf io)
	{
	}
	
	public IMessage onMessage(MessageRequestSelfUpdate m, MessageContext ctx)
	{
		ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(ctx.getServerHandler().playerEntity);
		return new MessageLMPlayerUpdate(p, true);
	}
}