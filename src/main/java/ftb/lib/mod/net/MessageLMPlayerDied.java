package ftb.lib.mod.net;

import ftb.lib.api.ForgePlayerSP;
import ftb.lib.api.ForgeWorldSP;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MessageLMPlayerDied extends MessageLM<MessageLMPlayerDied>
{
	public MessageLMPlayerDied() { }
	
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
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageLMPlayerDied m, MessageContext ctx)
	{
		if(ForgeWorldSP.inst == null) return null;
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(FTBLibClient.mc.thePlayer);
		p.onDeath();
		return null;
	}
}