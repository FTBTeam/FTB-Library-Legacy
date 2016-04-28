package ftb.lib.mod.net;

import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.config.ConfigGroup;
import ftb.lib.api.config.ServerConfigProvider;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.mod.client.gui.GuiEditConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageEditConfig m, MessageContext ctx)
	{
		ConfigGroup file = new ConfigGroup(m.configID);
		file.readFromNBT(m.nbt, true);
		FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, new ServerConfigProvider(m.token, m.reload, file)));
		return null;
	}
}