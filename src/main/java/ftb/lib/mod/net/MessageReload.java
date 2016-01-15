package ftb.lib.mod.net;

import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.mod.*;
import latmod.lib.*;
import latmod.lib.config.ConfigGroup;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageReload extends MessageLM
{
	public MessageReload() { super(ByteCount.INT); }
	
	public MessageReload(FTBWorld w)
	{
		this();
		w.writeReloadData(io);
		writeSyncedConfig(io);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		long ms = LMUtils.millis();
		FTBWorld.client.readReloadData(io);
		readSyncedConfig(io);
		reloadClient(ms, true);
		return null;
	}
	
	public static void reloadClient(long ms, boolean printMessage)
	{
		if(ms == 0L) ms = LMUtils.millis();
		GameModes.reload();
		EntityPlayer ep = FTBLibMod.proxy.getClientPlayer();
		EventFTBReload event = new EventFTBReload(FTBWorld.client, ep, true);
		if(FTBLib.ftbu != null) FTBLib.ftbu.onReloaded(event);
		event.post();
		if(printMessage)
			FTBLib.printChat(ep, new ChatComponentTranslation("ftbl:reloadedClient", ((LMUtils.millis() - ms) + "ms")));
	}
	
	static void writeSyncedConfig(ByteIOStream out)
	{
		try { ConfigRegistry.synced.write(out); }
		catch(Exception ex) {}
		if(FTBLibFinals.DEV) FTBLib.dev_logger.info("Synced config TX: " + ConfigRegistry.synced.getJson());
	}
	
	static void readSyncedConfig(ByteIOStream in)
	{
		ConfigGroup synced = new ConfigGroup(ConfigRegistry.synced.ID);
		try { synced.read(in); }
		catch(Exception ex) {}
		ConfigRegistry.synced.loadFromGroup(synced);
		if(FTBLibFinals.DEV) FTBLib.dev_logger.info("Synced config RX: " + synced.getJson());
	}
}