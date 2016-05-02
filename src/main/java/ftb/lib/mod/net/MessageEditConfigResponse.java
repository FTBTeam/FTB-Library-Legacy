package ftb.lib.mod.net;

import ftb.lib.FTBLib;
import ftb.lib.LMAccessToken;
import ftb.lib.api.config.ConfigFile;
import ftb.lib.api.config.ConfigGroup;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageEditConfigResponse extends MessageLM<MessageEditConfigResponse>
{
	public long token;
	public String configID;
	public boolean reload;
	public NBTTagCompound nbt;
	
	public MessageEditConfigResponse() { }
	
	public MessageEditConfigResponse(long t, boolean r, ConfigGroup o)
	{
		token = t;
		configID = o.getID();
		reload = r;
		nbt = new NBTTagCompound();
		o.writeToNBT(nbt, false);
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		token = io.readLong();
		configID = readString(io);
		reload = io.readBoolean();
		nbt = readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeLong(token);
		writeString(io, configID);
		io.writeBoolean(reload);
		writeTag(io, nbt);
	}
	
	@Override
	public IMessage onMessage(MessageEditConfigResponse m, MessageContext ctx)
	{
		EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
		if(!LMAccessToken.equals(ep, m.token, true)) { return null; }
		
		ConfigFile file = ConfigRegistry.map.containsKey(m.configID) ? ConfigRegistry.map.get(m.configID) : ConfigRegistry.getTempConfig(m.configID);
		if(file == null) { return null; }
		
		ConfigGroup group = new ConfigGroup(m.configID);
		group.readFromNBT(m.nbt, false);
		
		if(file.loadFromGroup(group, true) > 0)
		{
			file.save();
			
			if(m.reload)
			{
				FTBLib.reload(ep, true, false);
			}
		}
		
		return null;
	}
}