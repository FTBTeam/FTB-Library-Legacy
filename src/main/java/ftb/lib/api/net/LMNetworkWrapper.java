package ftb.lib.api.net;

import io.netty.channel.ChannelFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleChannelHandlerWrapper;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.relauncher.Side;

import java.util.EnumMap;

public class LMNetworkWrapper // SimpleNetworkWrapper
{
	public static LMNetworkWrapper newWrapper(String ID)
	{ return new LMNetworkWrapper(ID); }
	
	private final FMLEmbeddedChannel serverChannels;
	private final FMLEmbeddedChannel clientChannels;
	private SimpleIndexedCodec packetCodec;
	
	private LMNetworkWrapper(String s)
	{
		packetCodec = new SimpleIndexedCodec();
		EnumMap<Side, FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel(s, packetCodec);
		serverChannels = channels.get(Side.SERVER);
		clientChannels = channels.get(Side.CLIENT);
	}
	
	private FMLEmbeddedChannel get(Side s)
	{ return s.isServer() ? serverChannels : clientChannels; }
	
	@SuppressWarnings("all")
	public void register(Class<? extends MessageLM> c, int discriminator, Side s)
	{
		try
		{
			IMessageHandler<MessageLM, IMessage> h = c.newInstance();
			packetCodec.addDiscriminator(discriminator, c);
			FMLEmbeddedChannel channel = get(s);
			String type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
			channel.pipeline().addAfter(type, h.getClass().getName(), new SimpleChannelHandlerWrapper(h, s, c));
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public Packet getPacketFrom(IMessage message)
	{ return serverChannels.generatePacketFrom(message); }
	
	public void sendToAll(IMessage message)
	{
		serverChannels.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		serverChannels.writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	
	public void sendTo(IMessage message, EntityPlayerMP player)
	{
		serverChannels.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		serverChannels.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		serverChannels.writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	
	public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point)
	{
		serverChannels.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		serverChannels.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(point);
		serverChannels.writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	
	public void sendToDimension(IMessage message, int dimensionId)
	{
		serverChannels.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		serverChannels.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dimensionId);
		serverChannels.writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
	
	public void sendToServer(IMessage message)
	{
		clientChannels.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		clientChannels.writeAndFlush(message).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}
}