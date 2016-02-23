package ftb.lib.mod.net;

import ftb.lib.LMNBTUtils;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.net.*;
import ftb.lib.api.players.*;
import latmod.lib.ByteCount;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

import java.util.*;

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
		
		List<String> l = new ArrayList<>();
		
		for(ForgeWorldData d : LMWorldMP.inst.customData.values())
		{
			if(d.syncID()) l.add(d.ID);
		}
		
		io.writeShort(l.size());
		for(String s : l)
		{
			io.writeUTF(s);
		}
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
		
		LMWorldSP.inst.serverDataIDs.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
		{
			LMWorldSP.inst.serverDataIDs.add(io.readUTF());
		}
		
		if(first)
		{
			for(ForgeWorldData d : LMWorldSP.inst.customData.values())
				d.init();
		}
		
		MessageReload.reloadClient(0L, false);
		return null;
	}
}