package ftb.lib.mod.net;
import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.AdminToken;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigListRegistry;
import ftb.lib.mod.FTBLibMod;
import ftb.lib.mod.client.ServerConfigProvider;
import latmod.lib.config.ConfigList;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageEditConfigResponse extends MessageLM
{
	public MessageEditConfigResponse() { super(DATA_LONG); }
	
	public MessageEditConfigResponse(ServerConfigProvider provider)
	{
		this();
		io.writeLong(provider.adminToken);
		io.writeString(provider.list.ID);
		provider.list.writeToIO(io, false);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		if(!AdminToken.equals(ep, io.readLong())) return null;
		
		String id = io.readString();
		
		ConfigList list0 = ConfigListRegistry.instance.list.getObj(id);
		
		if(list0 != null)
		{
			ConfigList list = ConfigList.readFromIO(io, false);
			list.setID(id);
			
			if(list0.loadFromList(list))
			{
				if(list0.parentFile != null) list0.parentFile.save();
				FTBLibMod.reload(ep, true, true);
			}
		}
		
		return null;
	}
}