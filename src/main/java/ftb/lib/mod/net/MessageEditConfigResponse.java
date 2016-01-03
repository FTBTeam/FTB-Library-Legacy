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
	
	public MessageEditConfigResponse(ServerConfigProvider provider)
	{
		this();
		io.writeLong(provider.adminToken);
		io.writeBoolean(provider.isTemp);
		io.writeUTF(provider.group.ID);

		try { provider.group.write(io); }
		catch(Exception e) { }
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		if(!AdminToken.equals(ep, io.readLong())) return null;
		
		boolean isTemp = io.readBoolean();
		String id = io.readUTF();
		
		ConfigGroup group0 = isTemp ? ConfigRegistry.getTemp(true) : ConfigRegistry.map.get(id).getGroup();
		
		if(group0 != null)
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