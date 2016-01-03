package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.FTBLib;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.mod.FTBLibFinals;
import latmod.lib.ByteCount;
import latmod.lib.config.ConfigGroup;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageSyncConfig extends MessageLM
{
	public MessageSyncConfig() { super(ByteCount.INT); }
	
	public MessageSyncConfig(EntityPlayerMP ep)
	{
		this();

		try { ConfigRegistry.synced.write(io); }
		catch(Exception ex) {}

		if(FTBLibFinals.DEV) FTBLib.dev_logger.info("Synced config TX: " + ConfigRegistry.synced.getJson());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		ConfigGroup synced = new ConfigGroup(ConfigRegistry.synced.ID);

		try { synced.write(io); }
		catch(Exception ex) {}

		ConfigRegistry.synced.loadFromGroup(synced);
		if(FTBLibFinals.DEV) FTBLib.dev_logger.info("Synced config RX: " + synced.getJson());
		return null;
	}
}