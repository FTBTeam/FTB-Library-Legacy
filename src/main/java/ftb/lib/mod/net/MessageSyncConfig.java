package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.FTBLib;
import ftb.lib.api.*;
import ftb.lib.api.config.*;
import ftb.lib.mod.FTBLibFinals;
import latmod.lib.config.ConfigGroup;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageSyncConfig extends MessageLM
{
	public MessageSyncConfig() { super(DATA_LONG); }
	
	public MessageSyncConfig(EntityPlayerMP ep)
	{
		this();
		
		int count = 0;
		io.writeUShort(ConfigRegistry.synced.size());
		
		for(int i = 0; i < ConfigRegistry.synced.size(); i++)
		{
			ConfigGroup l = ConfigRegistry.synced.get(i);
			io.writeString(l.toString());
			l.write(io);
			count += l.getTotalEntryCount();
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
			ConfigGroup l = new ConfigGroup(id);
			l.read(io);
			count += l.getTotalEntryCount();
			
			ConfigGroup list = ConfigRegistry.list.getObj(l);
			if(list != null) list.loadFromGroup(l);
			else new EventSyncedConfig(l).post();
		}
		
		FTBLib.logger.info("Received " + count + " synced config values");
		
		return null;
	}
}