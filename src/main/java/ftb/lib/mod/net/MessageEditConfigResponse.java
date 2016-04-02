package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.*;
import ftb.lib.api.config.*;
import ftb.lib.api.net.*;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class MessageEditConfigResponse extends MessageLM // MessageEditConfig
{
	public MessageEditConfigResponse() { super(ByteCount.INT); }
	
	public MessageEditConfigResponse(long adminToken, boolean reload, ConfigGroup group)
	{
		this();
		io.writeLong(adminToken);
		io.writeUTF(group.getID());
		
		NBTTagCompound tag = new NBTTagCompound();
		group.writeToNBT(tag, false);
		writeTag(tag);
		
		io.writeBoolean(reload);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Response TX: " + group.getSerializableElement());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		if(!LMAccessToken.equals(ep, io.readLong(), false)) return null;
		String id = io.readUTF();
		
		ConfigFile file = ConfigRegistry.map.containsKey(id) ? ConfigRegistry.map.get(id) : ConfigRegistry.getTempConfig(id);
		if(file == null) return null;
		
		ConfigGroup group = new ConfigGroup(id);
		group.readFromNBT(readTag(), false);
		
		if(file.loadFromGroup(group, true) > 0)
		{
			file.save();
			if(io.readBoolean()) FTBLib.reload(ep, true, false);
		}
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Response RX: " + file.getSerializableElement());
		
		return null;
	}
}