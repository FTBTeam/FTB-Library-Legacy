package ftb.lib.api.net;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.*;

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
}