package ftb.lib.api.net;

import com.google.gson.JsonElement;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import latmod.lib.ByteIOStream;
import latmod.lib.json.JsonElementIO;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.UUID;

/**
 * Created by LatvianModder on 26.04.2016.
 */
public abstract class MessageLM<M extends MessageLM<M>> implements IMessage, IMessageHandler<M, IMessage>
{
	public abstract LMNetworkWrapper getWrapper();
	
	@Override
	public abstract void fromBytes(ByteBuf io);
	
	@Override
	public abstract void toBytes(ByteBuf io);
	
	/*
	@Override
	public void fromBytes(ByteBuf io)
	{
	}
	
	@Override
	public void toBytes(ByteBuf io)
	{
	}
	*/
	
	@Override
	public IMessage onMessage(M m, MessageContext ctx)
	{
		return null;
	}
	
	public final void sendTo(EntityPlayerMP ep)
	{
		//if(FTBLibFinals.DEV) FTBLib.logger.info("[S] Message sent: " + getClass().getName());
		if(ep != null) getWrapper().sendTo(this, ep);
		else getWrapper().sendToAll(this);
	}
	
	public final void sendToServer()
	{
		//if(FTBLibFinals.DEV) FTBLib.logger.info("[C] Message sent: " + getClass().getName());
		getWrapper().sendToServer(this);
	}
	
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
	
	public static JsonElement readJsonElement(ByteBuf io)
	{
		byte[] b = new byte[io.readInt()];
		io.readBytes(b, 0, b.length);
		ByteIOStream stream = new ByteIOStream();
		stream.setCompressedData(b);
		return JsonElementIO.read(stream);
	}
	
	public static void writeJsonElement(ByteBuf io, JsonElement e)
	{
		ByteIOStream stream = new ByteIOStream();
		JsonElementIO.write(stream, e);
		byte[] b = stream.toCompressedByteArray();
		io.writeInt(b.length);
		io.writeBytes(b, 0, b.length);
	}
}