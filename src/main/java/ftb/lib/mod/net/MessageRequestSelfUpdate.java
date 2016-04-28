package ftb.lib.mod.net;

import ftb.lib.api.ForgePlayerMP;
import ftb.lib.api.ForgeWorldMP;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageRequestSelfUpdate extends MessageLM<MessageRequestSelfUpdate>
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
	public IMessage onMessage(MessageRequestSelfUpdate m, MessageContext ctx)
	{
		ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(ctx.getServerHandler().playerEntity);
		return new MessageLMPlayerUpdate(p, true);
	}
}