package ftb.lib.api.net;

import io.netty.buffer.ByteBuf;
import latmod.lib.*;
import net.minecraftforge.fml.common.network.simpleimpl.*;

/**
 * Created by LatvianModder on 07.02.2016.
 */
public abstract class MessageLM_IO extends MessageLM<MessageLM_IO>
{
	public final ByteCount dataType;
	public ByteIOStream io;
	
	public MessageLM_IO(ByteCount t)
	{
		dataType = t;
		if(t != null) io = new ByteIOStream();
	}
	
	public final void fromBytes(ByteBuf bb)
	{
		if(dataType == null) return;
		
		int len = 0;
		
		if(dataType == ByteCount.BYTE) len = bb.readByte() & 0xFF;
		else if(dataType == ByteCount.SHORT) len = bb.readShort() & 0xFFFF;
		else if(dataType == ByteCount.INT) len = bb.readInt();
		
		byte[] b = new byte[len];
		bb.readBytes(b, 0, len);
		
		if(dataType == ByteCount.BYTE) io.setData(b);
		else io.setCompressedData(b);
	}
	
	public final void toBytes(ByteBuf bb)
	{
		if(dataType == null) return;
		
		byte[] b = (dataType == ByteCount.BYTE) ? io.toByteArray() : io.toCompressedByteArray();
		
		if(dataType == ByteCount.BYTE) bb.writeByte((byte) b.length);
		else if(dataType == ByteCount.SHORT) bb.writeShort((short) b.length);
		else if(dataType == ByteCount.INT) bb.writeInt(b.length);
		
		bb.writeBytes(b, 0, b.length);
	}
	
	public final IMessage onMessage(MessageLM_IO m, MessageContext ctx)
	{
		io = m.io;
		return onMessage(ctx);
	}
}
