package ftb.lib.mod.net;
import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.mod.client.ServerConfigProvider;
import latmod.lib.config.ConfigGroup;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageEditConfigResponse extends MessageLM // MessageEditConfig
{
	public MessageEditConfigResponse() { super(DATA_LONG); }
	
	public MessageEditConfigResponse(ServerConfigProvider provider)
	{
		this();
		io.writeLong(provider.adminToken);
		io.writeBoolean(provider.isTemp);
		io.writeString(provider.group.ID);
		provider.group.write(io);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		if(!AdminToken.equals(ep, io.readLong())) return null;
		
		boolean isTemp = io.readBoolean();
		String id = io.readString();
		
		ConfigGroup group0 = isTemp ? ConfigRegistry.getTemp(true) : ConfigRegistry.list.getObj(id);
		
		if(group0 != null)
		{
			ConfigGroup group = new ConfigGroup(id);
			group.read(io);
			
			if(group0.loadFromGroup(group) > 0)
			{
				if(group0.parentFile != null) group0.parentFile.save();
				FTBLib.reload(ep, true, true);
			}
		}
		
		return null;
	}
}