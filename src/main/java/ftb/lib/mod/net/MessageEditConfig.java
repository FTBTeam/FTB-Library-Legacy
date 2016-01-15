package ftb.lib.mod.net;

import ftb.lib.api.*;
import ftb.lib.client.FTBLibClient;
import ftb.lib.mod.client.ServerConfigProvider;
import ftb.lib.mod.client.gui.GuiEditConfig;
import latmod.lib.ByteCount;
import latmod.lib.config.ConfigGroup;
import net.minecraftforge.fml.common.network.simpleimpl.*;
import net.minecraftforge.fml.relauncher.*;

public class MessageEditConfig extends MessageLM // MessageEditConfigResponse
{
	public MessageEditConfig() { super(ByteCount.INT); }
	
	public MessageEditConfig(long t, ConfigGroup o)
	{
		this();
		io.writeLong(t);
		io.writeUTF(o.ID);
		o.writeExtended(io);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		long token = io.readLong();
		String id = io.readUTF();
		
		ConfigGroup group = new ConfigGroup(id);
		group.readExtended(io);
		
		FTBLibClient.mc.displayGuiScreen(new GuiEditConfig(FTBLibClient.mc.currentScreen, new ServerConfigProvider(token, group)));
		return null;
	}
}