package com.feed_the_beast.ftblib.lib.net;

import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleChannelHandlerWrapper;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.Map;

public class NetworkWrapper // SimpleNetworkWrapper
{
	private final SimpleIndexedCodec packetCodec;
	private final FMLEmbeddedChannel serverChannels;
	private final FMLEmbeddedChannel clientChannels;
	private int nextDiscriminator = 1;

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

	@SuppressWarnings("unchecked")
	public void register(@Nullable MessageToClient m)
	{
		if (m != null)
		{
			Class<? extends MessageToClient> clazz = m.getClass();
			packetCodec.addDiscriminator(nextDiscriminator, clazz);
			FMLEmbeddedChannel channel = getChannel(Side.CLIENT);
			String type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
			channel.pipeline().addAfter(type, clazz.getName(), new SimpleChannelHandlerWrapper(MessageToClientHandler.INSTANCE, Side.CLIENT, clazz));
		}

		nextDiscriminator++;
	}

	@SuppressWarnings("unchecked")
	public void register(@Nullable MessageToServer m)
	{
		if (m != null)
		{
			Class<? extends MessageToServer> clazz = m.getClass();
			packetCodec.addDiscriminator(nextDiscriminator, clazz);
			FMLEmbeddedChannel channel = getChannel(Side.SERVER);
			String type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
			channel.pipeline().addAfter(type, clazz.getName(), new SimpleChannelHandlerWrapper(MessageToServerHandler.INSTANCE, Side.SERVER, clazz));
		}

		nextDiscriminator++;
	}
}