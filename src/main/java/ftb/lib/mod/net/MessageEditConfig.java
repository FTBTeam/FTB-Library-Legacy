package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.config.*;
import ftb.lib.api.net.*;
import ftb.lib.mod.client.gui.GuiEditConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageEditConfig extends MessageLM<MessageEditConfig> // MessageEditConfigResponse
{
	public long token;
	public String configID;
	public boolean reload;
	public NBTTagCompound nbt;
	
	public MessageEditConfig() { }
	
	public MessageEditConfig(long t, boolean r, ConfigGroup o)
	{
		token = t;
		configID = o.getID();
		reload = r;
		nbt = new NBTTagCompound();
		o.writeToNBT(nbt, true);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public void fromBytes(ByteBuf io)
	{
		token = io.readLong();
		configID = readString(io);
		reload = io.readBoolean();
		nbt = readTag(io);
	}
	
	public void toBytes(ByteBuf io)
	{
		io.writeLong(token);
		writeString(io, configID);
		io.writeBoolean(reload);
		writeTag(io, nbt);
	}
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageEditConfig m, MessageContext ctx)
	{
		ConfigGroup file = new ConfigGroup(m.configID);
		file.readFromNBT(m.nbt, true);
		FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, new ServerConfigProvider(m.token, m.reload, file)));
		return null;
	}
}