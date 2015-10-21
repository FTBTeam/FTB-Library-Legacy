package ftb.lib.mod.net;

import cpw.mods.fml.common.network.simpleimpl.*;
import ftb.lib.api.config.ConfigSyncRegistry;
import io.netty.buffer.ByteBuf;
import latmod.lib.ByteIOStream;
import net.minecraft.entity.player.EntityPlayerMP;

public class MessageSyncConfig implements IMessage, IMessageHandler<MessageSyncConfig, IMessage>
{
	public final ByteIOStream io = new ByteIOStream();
	
	public MessageSyncConfig() { }
	
	public MessageSyncConfig(EntityPlayerMP ep)
	{
		ConfigSyncRegistry.writeToIO(io);
	}
	
	public void fromBytes(ByteBuf bb)
	{
		byte[] b = new byte[bb.readInt()];
		bb.readBytes(b, 0, b.length);
		io.setCompressedData(b);
	}
	
	public void toBytes(ByteBuf bb)
	{
		byte[] b = io.toCompressedByteArray();
		bb.writeInt(b.length);
		bb.writeBytes(b, 0, b.length);
	}
	
	public IMessage onMessage(MessageSyncConfig m, MessageContext ctx)
	{
		ConfigSyncRegistry.readFromIO(m.io);
		return null;
	}
}