package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.mod.client.ServerConfigProvider;
import latmod.lib.ByteCount;
import latmod.lib.config.ConfigGroup;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageEditConfigResponse extends MessageLM // MessageEditConfig
{
	public MessageEditConfigResponse() { super(ByteCount.INT); }
	
	public MessageEditConfigResponse(ServerConfigProvider provider, boolean remove, boolean data)
	{
		this();
		io.writeLong(provider.adminToken);
		io.writeBoolean(provider.isTemp);
		io.writeUTF(provider.group.ID);
		io.writeBoolean(remove);
		io.writeBoolean(data);

		if(data)
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
		
		boolean isTemp = io.readBoolean();
		String id = io.readUTF();
		boolean remove = io.readBoolean();
		boolean data = io.readBoolean();
		
		ConfigGroup group0 = isTemp ? ConfigRegistry.getTemp(id, remove) : ConfigRegistry.map.get(id).getGroup();
		
		if(group0 != null && data)
		{
			ConfigGroup group = new ConfigGroup(id);

			try { group.read(io); }
			catch(Exception e) { }

			if(group0.loadFromGroup(group) > 0)
			{
				if(group0.parentFile != null) group0.parentFile.save();
				FTBLib.reload(ep, true, true);
			}
		}
		
		return null;
	}
}