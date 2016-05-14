package ftb.lib.mod.net;

import ftb.lib.api.ForgePlayer;
import ftb.lib.api.ForgePlayerMP;
import ftb.lib.api.ForgeWorldMP;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageToServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class MessageModifyFriends extends MessageToServer<MessageModifyFriends>
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
		playerID = id;
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		actionID = io.readByte();
		playerID = actionID == ADD_ALL ? null : readUUID(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeByte(actionID);
		
		if(actionID != ADD_ALL)
		{
			writeUUID(io, playerID);
		}
	}
	
	@Override
	public void onMessage(MessageModifyFriends m, EntityPlayerMP ep)
	{
		ForgePlayerMP owner = ForgeWorldMP.inst.getPlayer(ep);
		
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
			if(p == null || p.equalsPlayer(owner)) { return; }
			
			switch(m.actionID)
			{
				case ADD:
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
					
					break;
				}
				case REMOVE:
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
					
					break;
				}
				case DENY:
				{
					if(p.friends.contains(owner.getProfile().getId()))
					{
						p.friends.remove(owner.getProfile().getId());
						owner.sendUpdate();
						p.sendUpdate();
						
						p.sendInfoUpdate(owner);
						owner.sendInfoUpdate(owner);
					}
					
					break;
				}
			}
		}
		
		owner.sendUpdate();
	}
}