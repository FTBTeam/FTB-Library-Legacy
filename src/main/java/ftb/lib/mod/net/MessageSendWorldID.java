package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.FTBWorld;
import ftb.lib.api.*;
import latmod.lib.ByteCount;

import java.util.UUID;

public class MessageSendWorldID extends MessageLM
{
	public UUID worldID;
	public String worldIDS;
	
	public MessageSendWorldID() { super(ByteCount.SHORT); }
	
	public MessageSendWorldID(FTBWorld w)
	{
		this();
		io.writeUUID(w.getWorldID());
		io.writeUTF(w.getWorldIDS());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		UUID id = io.readUUID();
		String ids = io.readUTF();
		
		if(FTBWorld.client == null || !FTBWorld.client.getWorldID().equals(id))
			FTBWorld.client = new FTBWorld(Side.CLIENT, id, ids);
		new EventFTBWorldClient(FTBWorld.client, false).post();
		return null;
	}
}