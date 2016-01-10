package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.mod.client.ServerConfigProvider;
import latmod.lib.*;
import latmod.lib.config.ConfigGroup;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageEditConfigResponse extends MessageLM // MessageEditConfig
{
	public static final int REMOVE = 1;
	public static final int SEND_DATA = 2;
	public static final int RELOAD = 3;
	public static final int TEMP = 4;
	public static final int SAVE = 5;
	
	public MessageEditConfigResponse() { super(ByteCount.INT); }
	
	public MessageEditConfigResponse(ServerConfigProvider provider, int... flgs)
	{
		this();
		io.writeLong(provider.adminToken);
		io.writeUTF(provider.group.ID);
		boolean[] flags = new boolean[8];
		for(int i : flgs) flags[i] = true;
		io.writeByte(Bits.toBits(flags));
		
		if(flags[SEND_DATA])
		{
			try { provider.group.write(io); }
			catch(Exception e) { }
		}
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		if(!LMAccessToken.equals(ep, io.readLong())) return null;
		String id = io.readUTF();
		boolean[] flags = new boolean[8];
		Bits.fromBits(flags, io.readUnsignedByte());
		
		ConfigRegistry.Provider provider = flags[TEMP] ? ConfigRegistry.tempMap.get(id) : ConfigRegistry.map.get(id);
		
		if(provider != null)
		{
			if(flags[SEND_DATA])
			{
				ConfigGroup group = new ConfigGroup(id);
				
				try { group.read(io); }
				catch(Exception e) { }
				
				if(provider.getGroup().loadFromGroup(group) > 0)
				{
					if(flags[SAVE] && provider.getGroup().parentFile != null) provider.getGroup().parentFile.save();
					if(flags[RELOAD]) FTBLib.reload(ep, true, true);
				}
			}
			
			if(flags[TEMP] && flags[REMOVE])
			{
				ConfigRegistry.tempMap.remove(provider.getID());
			}
		}
		
		return null;
	}
}