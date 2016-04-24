package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import ftb.lib.FTBLib;
import ftb.lib.LMAccessToken;
import ftb.lib.ReloadType;
import ftb.lib.api.config.ConfigFile;
import ftb.lib.api.config.ConfigGroup;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import latmod.lib.ByteCount;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class MessageEditConfigResponse extends MessageLM // MessageEditConfig
{
	public MessageEditConfigResponse() { super(ByteCount.INT); }
	
	public MessageEditConfigResponse(long adminToken, ReloadType reload, ConfigGroup group)
	{
		this();
		io.writeLong(adminToken);
		io.writeUTF(group.getID());
		
		NBTTagCompound tag = new NBTTagCompound();
		group.writeToNBT(tag, false);
		writeTag(tag);
		
		io.writeByte(reload.ordinal());
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Response TX: " + group.getSerializableElement());
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
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
			FTBLib.reload(ep, ReloadType.values()[io.readUnsignedByte()], false);
		}
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("Response RX: " + file.getSerializableElement());
		
		return null;
	}
}