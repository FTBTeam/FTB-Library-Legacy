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
	
	public MessageLMWorldUpdate(ForgePlayerMP self)
	{
		this();
		MessageReload.writeSyncedConfig(io);
		
		NBTTagCompound tag = new NBTTagCompound();
		ForgeWorldMP.inst.writeDataToNet(tag, self);
		LMNBTUtils.writeTag(io, tag);
		
		List<String> l = new ArrayList<>();
		
		for(ForgeWorldData d : ForgeWorldMP.inst.customData.values())
		{
			if(d.syncID()) l.add(d.getID());
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
		
		boolean first = ForgeWorldSP.inst == null;
		if(first) ForgeWorldSP.inst = new ForgeWorldSP(FTBLibClient.mc.getSession().getProfile());
		ForgeWorldSP.inst.readDataFromNet(LMNBTUtils.readTag(io), first);
		
		ForgeWorldSP.inst.serverDataIDs.clear();
		int s = io.readUnsignedShort();
		for(int i = 0; i < s; i++)
		{
			ForgeWorldSP.inst.serverDataIDs.add(io.readUTF());
		}
		
		if(first)
		{
			for(ForgeWorldData d : ForgeWorldSP.inst.customData.values())
				d.init();
		}
		
		MessageReload.reloadClient(0L, false);
		return null;
	}
}