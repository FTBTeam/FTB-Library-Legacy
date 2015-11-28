package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.FTBLib;
import ftb.lib.api.*;
import ftb.lib.api.config.*;
import ftb.lib.mod.FTBLibFinals;
import latmod.lib.config.ConfigList;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageSyncConfig extends MessageLM
{
	public MessageSyncConfig() { super(DATA_LONG); }
	
	public MessageSyncConfig(EntityPlayerMP ep)
	{
		this();
		
		int count = 0;
		io.writeUShort(ConfigListRegistry.instance.synced.size());
		
		for(int i = 0; i < ConfigListRegistry.instance.synced.size(); i++)
		{
			ConfigList l = ConfigListRegistry.instance.synced.get(i);
			io.writeString(l.toString());
			count += l.writeToIO(io, false);
		}
		
		if(FTBLibFinals.DEV) FTBLib.dev_logger.info("Sent " + count + " synced config values");
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		int count = 0;
		
		int s = io.readUShort();
		
		for(int i = 0; i < s; i++)
		{
			String id = io.readString();
			ConfigList l = ConfigList.readFromIO(io, false);
			l.setID(id);
			count += l.totalEntryCount();
			
			ConfigList list = ConfigListRegistry.instance.list.getObj(l);
			if(list != null) list.loadFromList(l);
			else new EventSyncedConfig(l).post();
		}
		
		FTBLib.logger.info("Received " + count + " synced config values");
		
		return null;
	}
}