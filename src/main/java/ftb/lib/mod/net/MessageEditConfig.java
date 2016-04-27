package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ftb.lib.FTBLib;
import ftb.lib.ReloadType;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.config.ConfigGroup;
import ftb.lib.api.config.ServerConfigProvider;
import ftb.lib.api.net.LMNetworkWrapper;
import ftb.lib.api.net.MessageLM;
import ftb.lib.mod.client.gui.GuiEditConfig;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

public class MessageEditConfig extends MessageLM<MessageEditConfig>
{
	public long token;
	public int typeID;
	public String groupID;
	public NBTTagCompound tag;
	
	public MessageEditConfig() { }
	
	public MessageEditConfig(long t, ReloadType reload, ConfigGroup group)
	{
		token = t;
		typeID = reload.ordinal();
		groupID = group.getID();
		tag = new NBTTagCompound();
		group.writeToNBT(tag, true);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("TX Send: " + group.getSerializableElement());
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	public void fromBytes(ByteBuf io)
	{
		token = io.readLong();
		typeID = io.readUnsignedByte();
		groupID = readString(io);
		tag = readTag(io);
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
		io.writeLong(token);
		io.writeByte(typeID);
		writeString(io, groupID);
		writeTag(io, tag);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageEditConfig m, MessageContext ctx)
	{
		ConfigGroup group = new ConfigGroup(m.groupID);
		group.readFromNBT(m.tag, true);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("RX Send: " + group.getSerializableElement());
		
		FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, new ServerConfigProvider(m.token, ReloadType.values()[m.typeID], group)));
		return null;
	}
}