package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.FTBLib;
import ftb.lib.FTBWorld;
import ftb.lib.api.EventFTBWorldClient;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageSendWorldID extends MessageLM
{
	public MessageSendWorldID() { super(ByteCount.INT); }
	
	public MessageSendWorldID(FTBWorld w, EntityPlayerMP ep)
	{
		this();
		MessageReload.writeSyncedConfig(io);
		io.writeBoolean(FTBLib.ftbu != null);
		w.writeReloadData(io);
		if(FTBLib.ftbu != null) FTBLib.ftbu.writeWorldData(io, ep);
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public IMessage onMessage(MessageContext ctx)
	{
		MessageReload.readSyncedConfig(io);
		boolean hasFTBU = io.readBoolean();
		
		boolean first = FTBWorld.client == null;
		if(first) FTBWorld.client = new FTBWorld(Side.CLIENT);
		FTBWorld.client.readReloadData(io);
		new EventFTBWorldClient(FTBWorld.client).post();
		if(first && hasFTBU && FTBLib.ftbu != null) FTBLib.ftbu.readWorldData(io);
		
		return null;
	}
}