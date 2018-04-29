package com.feed_the_beast.ftblib.lib.net;

import io.netty.channel.ChannelFutureListener;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.network.FMLEmbeddedChannel;
import net.minecraftforge.fml.common.network.FMLOutboundHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public abstract class MessageToClient<E extends MessageToClient<E>> extends MessageBase<E>
{
	public final void sendTo(EntityPlayerMP player)
	{
		if (player.connection == null || player instanceof FakePlayer)
		{
			return;
		}

		FMLEmbeddedChannel channel = getWrapper().getChannel(Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.PLAYER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(player);
		channel.writeAndFlush(this).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	public final void sendToAll()
	{
		FMLEmbeddedChannel channel = getWrapper().getChannel(Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALL);
		channel.writeAndFlush(this).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	public final void sendToDimension(int dim)
	{
		FMLEmbeddedChannel channel = getWrapper().getChannel(Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.DIMENSION);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(dim);
		channel.writeAndFlush(this).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	public final void sendToAllAround(int dim, BlockPos pos, double range)
	{
		FMLEmbeddedChannel channel = getWrapper().getChannel(Side.SERVER);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGET).set(FMLOutboundHandler.OutboundTarget.ALLAROUNDPOINT);
		channel.attr(FMLOutboundHandler.FML_MESSAGETARGETARGS).set(new NetworkRegistry.TargetPoint(dim, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, range));
		channel.writeAndFlush(this).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
	}

	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
	}
}