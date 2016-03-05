package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.*;
import ftb.lib.api.players.*;
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
		if(ForgeWorldSP.inst == null) return null;
		ForgePlayerSP p = ForgeWorldSP.inst.getPlayer(FTBLibClient.mc.thePlayer);
		p.onDeath();
		return null;
	}
}