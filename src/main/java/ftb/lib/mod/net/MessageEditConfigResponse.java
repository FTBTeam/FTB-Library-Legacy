package ftb.lib.mod.net;

import ftb.lib.*;
import ftb.lib.api.config.*;
import ftb.lib.api.net.*;
import latmod.lib.ByteCount;
import latmod.lib.config.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.*;

public class MessageEditConfigResponse extends MessageLM_IO // MessageEditConfig
{
	public MessageEditConfigResponse() { super(ByteCount.INT); }
	
	public MessageEditConfigResponse(ServerConfigProvider provider)
	{
		this();
		io.writeLong(provider.adminToken);
		io.writeUTF(provider.getConfigFile().getID());
		
		try { provider.getConfigFile().write(io); }
		catch(Exception e) { }
		
		io.writeBoolean(provider.reload);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		if(!LMAccessToken.equals(ep, io.readLong(), true)) return null;
		String id = io.readUTF();
		
		ConfigFile file = ConfigRegistry.map.containsKey(id) ? ConfigRegistry.map.get(id) : ConfigRegistry.getTempConfig(id);
		if(file == null) return null;
		
		ConfigGroup group = new ConfigGroup(id);
		
		try { group.read(io); }
		catch(Exception e) { }
		
		if(file.loadFromGroup(group) > 0)
		{
			file.save();
			if(io.readBoolean()) FTBLib.reload(ep, true, false);
		}
		
		return null;
	}
}