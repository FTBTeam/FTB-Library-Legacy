package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.FTBLib;
import ftb.lib.api.client.FTBLibClient;
import ftb.lib.api.config.*;
import ftb.lib.api.net.*;
import ftb.lib.mod.client.gui.GuiEditConfig;
import latmod.lib.ByteCount;

public class MessageEditConfig extends MessageLM // MessageEditConfigResponse
{
	public MessageEditConfig() { super(ByteCount.INT); }
	
	public MessageEditConfig(long t, boolean reload, ConfigFile file)
	{
		this();
		io.writeLong(t);
		io.writeUTF(file.getID());
		io.writeBoolean(reload);
		file.writeExtended(io);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("TX Send: " + file.getSerializableElement());
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		long token = io.readLong();
		String id = io.readUTF();
		boolean reload = io.readBoolean();
		
		ConfigFile file = new ConfigFile(id);
		file.readExtended(io);
		
		if(FTBLib.DEV_ENV) FTBLib.dev_logger.info("RX Send: " + file.getSerializableElement());
		
		FTBLibClient.openGui(new GuiEditConfig(FTBLibClient.mc.currentScreen, new ServerConfigProvider(token, reload, file)));
		return null;
	}
}