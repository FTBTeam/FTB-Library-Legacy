package ftb.lib.mod.net;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.FTBWorld;
import io.netty.buffer.ByteBuf;

public class MessageSendGameMode implements IMessage, IMessageHandler<MessageSendGameMode, IMessage>
{
	public String mode;
	
	public MessageSendGameMode() { }
	
	public MessageSendGameMode(String s)
	{ mode = s; }
	
	public void fromBytes(ByteBuf io)
	{ mode = ByteBufUtils.readUTF8String(io); }
	
	public void toBytes(ByteBuf io)
	{ ByteBufUtils.writeUTF8String(io, mode); }
	
	public IMessage onMessage(MessageSendGameMode m, MessageContext ctx)
	{ FTBWorld.client.setMode(Side.CLIENT, m.mode, true); return null; }
}