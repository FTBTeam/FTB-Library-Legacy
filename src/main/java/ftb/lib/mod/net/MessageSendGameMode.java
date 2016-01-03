package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.FTBWorld;
import ftb.lib.api.*;
import latmod.lib.ByteCount;

public class MessageSendGameMode extends MessageLM
{
	public MessageSendGameMode() { super(ByteCount.SHORT); }
	
	public MessageSendGameMode(String s)
	{ this(); io.writeUTF(s); }
	
	public LMNetworkWrapper getWrapper()
	{ return FTBLibNetHandler.NET; }
	
	public IMessage onMessage(MessageContext ctx)
	{ FTBWorld.client.setMode(io.readUTF(), true); return null; }
}