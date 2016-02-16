package ftb.lib.mod.net;

import ftb.lib.LMNBTUtils;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.friends.*;
import ftb.lib.api.net.*;
import latmod.lib.ByteCount;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageLMWorldUpdate extends MessageLM_IO
{
	public MessageLMWorldUpdate() { super(ByteCount.INT); }
	
	public MessageLMWorldUpdate(LMPlayerMP self)
	{
		this();
		MessageReload.writeSyncedConfig(io);
		
		NBTTagCompound tag = new NBTTagCompound();
		LMWorldMP.inst.writeDataToNet(tag, self);
		LMNBTUtils.writeTag(io, tag);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		MessageReload.readSyncedConfig(io);
		
		boolean first = LMWorldSP.inst == null;
		if(first) LMWorldSP.inst = new LMWorldSP(FTBLibClient.mc.getSession().getProfile());
		LMWorldSP.inst.readDataFromNet(LMNBTUtils.readTag(io), first);
		
		MessageReload.reloadClient(0L, false);
		return null;
	}
}