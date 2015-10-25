package ftb.lib.mod.net;

import java.util.UUID;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.FTBWorld;
import ftb.lib.api.*;

public class MessageSendWorldID extends MessageLM
{
	public UUID worldID;
	public String worldIDS;
	
	public MessageSendWorldID() { super(DATA_SHORT); }
	
	public MessageSendWorldID(FTBWorld w)
	{
		this();
		io.writeUUID(w.getWorldID());
		io.writeString(w.getWorldIDS());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		UUID id = io.readUUID();
		String ids = io.readString();
		
		if(FTBWorld.client == null || !FTBWorld.client.getWorldID().equals(id))
			FTBWorld.client = new FTBWorld(id, ids);
		new EventFTBWorldClient(FTBWorld.client, false).post();
		return null;
	}
}