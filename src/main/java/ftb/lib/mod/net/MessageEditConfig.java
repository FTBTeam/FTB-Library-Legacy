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
import latmod.lib.ByteCount;
import net.minecraft.nbt.NBTTagCompound;

public class MessageEditConfig extends MessageLM // MessageEditConfigResponse
{
	public MessageEditConfig() { super(ByteCount.INT); }
	
	public MessageEditConfig(long t, ReloadType reload, ConfigGroup group)
	{
		this();
		io.writeLong(t);
		io.writeUTF(group.getID());
		io.writeByte(reload.ordinal());
		
		NBTTagCompound tag = new NBTTagCompound();
		group.writeToNBT(tag, true);
		writeTag(tag);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("TX Send: " + group.getSerializableElement());
	}
	
	@Override
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@Override
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		long token = io.readLong();
		String id = io.readUTF();
		ReloadType reload = ReloadType.values()[io.readUnsignedByte()];
		
		ConfigGroup group = new ConfigGroup(id);
		group.readFromNBT(readTag(), true);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("RX Send: " + group.getSerializableElement());
		
		FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, new ServerConfigProvider(token, reload, group)));
		return null;
	}
}