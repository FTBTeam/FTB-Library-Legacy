package ftb.lib;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.*;
import cpw.mods.fml.relauncher.Side;
import ftb.lib.mod.FTBLib;
import io.netty.buffer.ByteBuf;

public class MessageSetMode implements IMessage, IMessageHandler<MessageSetMode, IMessage>
{
	public String mode;
	
	public MessageSetMode() { }
	
	public MessageSetMode(String s)
	{ mode = s; }
	
	public void fromBytes(ByteBuf io)
	{ mode = ByteBufUtils.readUTF8String(io); }
	
	public void toBytes(ByteBuf io)
	{ ByteBufUtils.writeUTF8String(io, mode); }
	
	public IMessage onMessage(MessageSetMode m, MessageContext ctx)
	{ FTBLib.setMode(Side.CLIENT, m.mode, true, false); return null; }
}