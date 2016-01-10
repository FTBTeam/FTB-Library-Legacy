package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.*;
import ftb.lib.api.*;
import ftb.lib.api.config.ConfigRegistry;
import ftb.lib.client.FTBLibClient;
import ftb.lib.mod.client.ServerConfigProvider;
import ftb.lib.mod.client.gui.GuiEditConfig;
import latmod.lib.ByteCount;
import latmod.lib.config.ConfigGroup;

public class MessageEditConfig extends MessageLM // MessageEditConfigResponse
{
	public MessageEditConfig() { super(ByteCount.INT); }
	
	public MessageEditConfig(long t, boolean temp, ConfigRegistry.Provider p)
	{
		this();
		io.writeLong(t);
		io.writeBoolean(temp);
		io.writeUTF(p.getID());
		
		try { p.getGroup().writeExtended(io); }
		catch(Exception e) { }
	}
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	@SideOnly(Side.CLIENT)
	public IMessage onMessage(MessageContext ctx)
	{
		long token = io.readLong();
		boolean temp = io.readBoolean();
		String id = io.readUTF();
		ConfigGroup group = new ConfigGroup(id);
		
		try { group.readExtended(io); }
		catch(Exception e) { }
		
		FTBLibClient.mc.displayGuiScreen(new GuiEditConfig(FTBLibClient.mc.currentScreen, new ServerConfigProvider(token, temp, group)));
		return null;
	}
}