package ftb.lib.mod.net;

import ftb.lib.api.*;
import ftb.lib.api.net.*;
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
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET_INFO; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		playerID = readUUID(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		writeUUID(io, playerID);
	}
	
	@Override
	public IMessage onMessage(MessageRequestPlayerInfo m, MessageContext ctx)
	{
		ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(m.playerID);
		if(p != null)
		{
			return new MessageLMPlayerInfo(ForgeWorldMP.inst.getPlayer(ctx.getServerHandler().playerEntity), p);
		}
		return null;
	}
}