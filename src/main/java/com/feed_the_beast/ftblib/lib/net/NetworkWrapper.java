package com.feed_the_beast.ftblib.lib.net;

import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleChannelHandlerWrapper;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public class NetworkWrapper // SimpleNetworkWrapper
{
	private final SimpleIndexedCodec packetCodec;
	private final FMLEmbeddedChannel serverChannels;
	private final FMLEmbeddedChannel clientChannels;

	private NetworkWrapper(String s)
	{
		packetCodec = new SimpleIndexedCodec();
		Map<Side, FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel(s, packetCodec);
		serverChannels = channels.get(Side.SERVER);
		clientChannels = channels.get(Side.CLIENT);
	}

	public static NetworkWrapper newWrapper(String id)
	{
		if (id.length() > 20)
		{
			throw new IllegalArgumentException("Network wrapper " + id + " id isn't valid, must be <= 20 characters!");
		}

		return new NetworkWrapper(id);
	}

	public FMLEmbeddedChannel getChannel(Side s)
	{
		return s.isServer() ? serverChannels : clientChannels;
	}

	public void register(int discriminator, MessageToClient<?> m)
	{
		packetCodec.addDiscriminator(discriminator, m.getClass());
		FMLEmbeddedChannel channel = getChannel(Side.CLIENT);
		String type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
		channel.pipeline().addAfter(type, m.getClass().getName(), new SimpleChannelHandlerWrapper(MessageToClientHandler.INSTANCE, Side.CLIENT, m.getClass()));
	}

	public void register(int discriminator, MessageToServer<?> m)
	{
		packetCodec.addDiscriminator(discriminator, m.getClass());
		FMLEmbeddedChannel channel = getChannel(Side.SERVER);
		String type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
		channel.pipeline().addAfter(type, m.getClass().getName(), new SimpleChannelHandlerWrapper(MessageToServerHandler.INSTANCE, Side.SERVER, m.getClass()));
	}
}