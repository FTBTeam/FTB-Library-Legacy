package ftb.lib.mod.net;

import ftb.lib.api.friends.*;
import ftb.lib.api.net.*;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.UUID;

public class MessageLMPlayerLoggedOut extends MessageLM<MessageLMPlayerLoggedOut>
{
	public UUID playerID;
	
	public MessageLMPlayerLoggedOut() { }
	
	public MessageLMPlayerLoggedOut(LMPlayerMP p)
	{
		playerID = p.getProfile().getId();
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
	}
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageLMPlayerLoggedOut m, MessageContext ctx)
	{
		LMPlayerSP p = LMWorldSP.inst.getPlayer(m.playerID);
		p.onLoggedOut();
		p.isOnline = false;
		return null;
	}
}