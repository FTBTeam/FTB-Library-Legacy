package com.feed_the_beast.ftblib.lib.net;

import io.netty.channel.ChannelFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public abstract class MessageToServer extends MessageBase
{
	public final void sendToServer()
	{
		FMLEmbeddedChannel channel = getWrapper().getChannel(Side.CLIENT);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.TOSERVER);
		channel.writeAndFlush(this).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	public void onMessage(EntityPlayerMP player)
	{
	}
}