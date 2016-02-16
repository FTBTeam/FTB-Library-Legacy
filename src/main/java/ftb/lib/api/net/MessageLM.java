package ftb.lib.api.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.*;

import java.util.UUID;

public abstract class MessageLM<E extends MessageLM<E>> implements IMessage, IMessageHandler<E, IMessage>
{
	public abstract LMNetworkWrapper getWrapper();
	
	public IMessage onMessage(MessageContext ctx)
	{ return null; }
	
	public abstract void fromBytes(ByteBuf io);
	public abstract void toBytes(ByteBuf io);
	
	/*
	public void fromBytes(ByteBuf io)
	{
	}
	
	public void toBytes(ByteBuf io)
	{
	}
	*/
	
	public IMessage onMessage(E m, MessageContext ctx)
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
}