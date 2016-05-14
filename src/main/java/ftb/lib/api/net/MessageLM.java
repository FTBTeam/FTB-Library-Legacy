package ftb.lib.api.net;

import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import latmod.lib.ByteIOStream;
import latmod.lib.json.JsonElementIO;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

public abstract class MessageLM<E extends MessageLM<E>> implements IMessage, IMessageHandler<E, IMessage>
{
	MessageLM()
	{
	}
	
	public abstract LMNetworkWrapper getWrapper();
	abstract Side getReceivingSide();
	
	@Override
	public abstract void toBytes(ByteBuf io);
	
	@Override
	public abstract void fromBytes(ByteBuf io);
	
	/*
	public void toBytes(ByteBuf io)
	{
	}
	
	public void fromBytes(ByteBuf io)
	{
	}
	*/
	
	@Override
	public IMessage onMessage(E m, MessageContext ctx)
	{
		return null;
	}
	
	public final void register(int id)
	{ getWrapper().register(this, id); }
	
	// Helper methods //
	
	public static UUID readUUID(ByteBuf io)
	{
		long msb = io.readLong();
		long lsb = io.readLong();
		return new UUID(msb, lsb);
	}
	
	public static void writeUUID(ByteBuf io, UUID id)
	{
		io.writeLong(id.getMostSignificantBits());
		io.writeLong(id.getLeastSignificantBits());
	}
	
	public static String readString(ByteBuf io)
	{ return ByteBufUtils.readUTF8String(io); }
	
	public static void writeString(ByteBuf io, String s)
	{ ByteBufUtils.writeUTF8String(io, s); }
	
	public static NBTTagCompound readTag(ByteBuf io)
	{ return ByteBufUtils.readTag(io); }
	
	public static void writeTag(ByteBuf io, NBTTagCompound tag)
	{ ByteBufUtils.writeTag(io, tag); }
	
	//TODO: Improve me
	public static JsonElement readJsonElement(ByteBuf io)
	{
		byte[] b = new byte[io.readInt()];
		io.readBytes(b, 0, b.length);
		ByteIOStream stream = new ByteIOStream();
		stream.setCompressedData(b);
		return JsonElementIO.read(stream);
	}
	
	//TODO: Improve me
	public static void writeJsonElement(ByteBuf io, JsonElement e)
	{
		ByteIOStream stream = new ByteIOStream();
		JsonElementIO.write(stream, e);
		byte[] b = stream.toCompressedByteArray();
		io.writeInt(b.length);
		io.writeBytes(b, 0, b.length);
	}
}