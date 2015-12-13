package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.*;
import ftb.lib.client.FTBLibClient;
import ftb.lib.mod.client.ServerConfigProvider;
import ftb.lib.mod.client.gui.GuiEditConfig;
import latmod.lib.config.ConfigGroup;

public class MessageEditConfig extends MessageLM // MessageEditConfigResponse
{
	public MessageEditConfig() { super(DATA_LONG); }
	
	public MessageEditConfig(long t, boolean temp, ConfigGroup group)
	{
		this();
		io.writeLong(t);
		io.writeBoolean(temp);
		io.writeString(group.ID);
		group.writeExtended(io);
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		long token = io.readLong();
		boolean temp = io.readBoolean();
		String id = io.readString();
		
		ConfigGroup group = new ConfigGroup(id);
		group.readExtended(io);
		FTBLibClient.mc.displayGuiScreen(new GuiEditConfig(null, new ServerConfigProvider(token, temp, group)));
		return null;
	}
}