package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.FTBWorld;
import ftb.lib.api.*;

public class MessageSendGameMode extends MessageLM
{
	public MessageSendGameMode() { super(DATA_SHORT); }
	
	public MessageSendGameMode(String s)
	{ this(); io.writeString(s); }
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{ FTBWorld.client.setMode(Side.CLIENT, io.readString(), true); return null; }
}