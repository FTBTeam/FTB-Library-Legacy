package com.feed_the_beast.ftbl.lib.net;

import io.netty.channel.ChannelFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleChannelHandlerWrapper;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleIndexedCodec;
import net.minecraftforge.fml.relauncher.Side;

import java.util.Map;

public class LMNetworkWrapper // SimpleNetworkWrapper
{
    private final SimpleIndexedCodec packetCodec;
    private final FMLEmbeddedChannel serverChannels;
    private final FMLEmbeddedChannel clientChannels;

    private LMNetworkWrapper(String s)
    {
        packetCodec = new SimpleIndexedCodec();
        Map<Side, FMLEmbeddedChannel> channels = NetworkRegistry.INSTANCE.newChannel(s, packetCodec);
        serverChannels = channels.get(Side.SERVER);
        clientChannels = channels.get(Side.CLIENT);
    }

    public static LMNetworkWrapper newWrapper(String ID)
    {
        return new LMNetworkWrapper(ID);
    }

    private FMLEmbeddedChannel get(Side s)
    {
        return s.isServer() ? serverChannels : clientChannels;
    }

    public void register(int discriminator, MessageLM<?> m)
    {
        try
        {
            packetCodec.addDiscriminator(discriminator, m.getClass());
            FMLEmbeddedChannel channel = get(m.getReceivingSide());
            String type = channel.findChannelHandlerNameForType(SimpleIndexedCodec.class);
            channel.pipeline().addAfter(type, m.getClass().getName(), new SimpleChannelHandlerWrapper(m, m.getReceivingSide(), m.getClass()));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public Packet getPacketFrom(IMessage message)
    {
        return serverChannels.generatePacketFrom(message);
    }

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