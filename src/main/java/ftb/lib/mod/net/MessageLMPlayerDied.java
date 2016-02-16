package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.friends.*;
import ftb.lib.api.net.*;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageLMPlayerDied extends MessageLM<MessageLMPlayerDied>
{
	public MessageLMPlayerDied() { }
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public void fromBytes(ByteBuf io)
	{
	}
	
	public void toBytes(ByteBuf io)
	{
	}
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageLMPlayerDied m, MessageContext ctx)
	{
		if(LMWorldSP.inst == null) return null;
		LMPlayerSP p = LMWorldSP.inst.getPlayer(FTBLibClient.mc.thePlayer);
		p.onDeath();
		return null;
	}
}