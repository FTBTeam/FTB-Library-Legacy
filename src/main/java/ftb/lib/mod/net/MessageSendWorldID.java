package ftb.lib.mod.net;

import java.util.UUID;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.FTBWorld;
import ftb.lib.api.EventFTBWorldClient;
import io.netty.buffer.ByteBuf;

public class MessageSendWorldID implements IMessage, IMessageHandler<MessageSendWorldID, IMessage>
{
	public UUID worldID;
	public String worldIDS;
	public String mode;
	
	public MessageSendWorldID() { }
	
	public MessageSendWorldID(FTBWorld w)
	{
		worldID = w.getWorldID();
		worldIDS = w.getWorldIDS();
		mode = w.getMode();
	}
	
	public void fromBytes(ByteBuf io)
	{
		long msb = io.readLong();
		long lsb = io.readLong();
		worldID = new UUID(msb, lsb);
		worldIDS = ByteBufUtils.readUTF8String(io);
		mode = ByteBufUtils.readUTF8String(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		io.writeLong(worldID.getMostSignificantBits());
		io.writeLong(worldID.getLeastSignificantBits());
		ByteBufUtils.writeUTF8String(io, worldIDS);
		ByteBufUtils.writeUTF8String(io, mode);
	}
	
	public IMessage onMessage(MessageSendWorldID m, MessageContext ctx)
	{
		if(FTBWorld.client == null || !FTBWorld.client.getWorldID().equals(m.worldID))
			FTBWorld.client = new FTBWorld(m.worldID, m.worldIDS);
		new EventFTBWorldClient(FTBWorld.client, false).post();
		FTBWorld.client.setMode(Side.CLIENT, m.mode, true);
		return null;
	}
}