package ftb.lib.mod.net;

import ftb.lib.api.*;
import ftb.lib.api.net.*;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.*;

import java.util.UUID;

public class MessageModifyFriends extends MessageLM<MessageModifyFriends>
{
	public static final byte ADD = 0;
	public static final byte REMOVE = 1;
	public static final byte ADD_ALL = 2;
	public static final byte DENY = 3;
	
	public byte actionID;
	public UUID playerID;
	
	public MessageModifyFriends() { }
	
	public MessageModifyFriends(byte a, UUID id)
	{
		actionID = a;
		playerID = (id == null) ? new UUID(0L, 0L) : id;
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		actionID = io.readByte();
		long msb = io.readLong();
		long lsb = io.readLong();
		playerID = new UUID(msb, lsb);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeByte(actionID);
		io.writeLong(playerID.getMostSignificantBits());
		io.writeLong(playerID.getLeastSignificantBits());
	}
	
	@Override
	public IMessage onMessage(MessageModifyFriends m, MessageContext ctx)
	{
		ForgePlayerMP owner = ForgeWorldMP.inst.getPlayer(ctx.getServerHandler().playerEntity);
		
		if(m.actionID == ADD_ALL)
		{
			for(ForgePlayer p : owner.getWorld().playerMap.values())
			{
				if(!p.equalsPlayer(owner) && p.isFriendRaw(owner) && !owner.isFriendRaw(p))
				{
					owner.friends.add(p.getProfile().getId());
					p.toPlayerMP().sendUpdate();
				}
			}
		}
		else
		{
			ForgePlayerMP p = ForgeWorldMP.inst.getPlayer(m.playerID);
			if(p == null || p.equalsPlayer(owner)) return null;
			
			if(m.actionID == ADD)
			{
				if(!owner.isFriendRaw(p))
				{
					owner.friends.add(p.getProfile().getId());
					owner.sendUpdate();
					p.sendUpdate();
					p.checkNewFriends();
					
					p.sendInfoUpdate(owner);
					owner.sendInfoUpdate(owner);
				}
			}
			else if(m.actionID == REMOVE)
			{
				if(owner.friends.contains(p.getProfile().getId()))
				{
					owner.friends.remove(p.getProfile().getId());
					owner.sendUpdate();
					p.sendUpdate();
					p.checkNewFriends();
					
					p.sendInfoUpdate(owner);
					owner.sendInfoUpdate(owner);
				}
			}
			else if(m.actionID == DENY)
			{
				if(p.friends.contains(owner.getProfile().getId()))
				{
					p.friends.remove(owner.getProfile().getId());
					owner.sendUpdate();
					p.sendUpdate();
					
					p.sendInfoUpdate(owner);
					owner.sendInfoUpdate(owner);
				}
			}
		}
		
		owner.sendUpdate();
		return null;
	}
}