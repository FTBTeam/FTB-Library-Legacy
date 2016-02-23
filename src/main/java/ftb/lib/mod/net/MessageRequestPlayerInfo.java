package ftb.lib.mod.net;

import ftb.lib.api.net.*;
import ftb.lib.api.players.*;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.*;

import java.util.UUID;

public class MessageRequestPlayerInfo extends MessageLM<MessageRequestPlayerInfo>
{
	public UUID playerID;
	
	public MessageRequestPlayerInfo() { }
	
	public MessageRequestPlayerInfo(UUID player)
	{
		playerID = player;
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_INFO; }
	
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
	}
	
	public IMessage onMessage(MessageRequestPlayerInfo m, MessageContext ctx)
	{
		LMPlayerMP p = LMWorldMP.inst.getPlayer(m.playerID);
		if(p != null)
		{
			return new MessageLMPlayerInfo(LMWorldMP.inst.getPlayer(ctx.getServerHandler().playerEntity), p);
		}
		return null;
	}
}