package com.feed_the_beast.ftblib.lib.net;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.util.CommonUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

public abstract class MessageBase<E extends MessageBase<E>> implements IMessage, IMessageHandler<E, IMessage>
{
	MessageBase()
	{
	}

	public abstract NetworkWrapper getWrapper();

	abstract Side getReceivingSide();

	public boolean hasData()
	{
		return true;
	}

	@Override
	public final void toBytes(ByteBuf buf)
	{
		if (hasData())
		{
			writeData(new DataOut(buf));
		}
	}

	@Override
	public final void fromBytes(ByteBuf buf)
	{
		if (hasData())
		{
			readData(new DataIn(buf));
		}
	}

	@Override
	@Nullable
	public final IMessage onMessage(final E message, final MessageContext context)
	{
		if (getReceivingSide().isServer())
		{
			context.getServerHandler().player.mcServer.addScheduledTask(() ->
			{
				message.onMessage(CommonUtils.cast(message), context.getServerHandler().player);

				if (FTBLibConfig.general.log_net)
				{
					CommonUtils.DEV_LOGGER.info("Net TX: " + message.getClass().getName());
				}
			});
		}
		else
		{
			FTBLib.PROXY.handleClientMessage(message);
		}

		return null;
	}

	public void writeData(DataOut data)
	{
	}

	public void readData(DataIn data)
	{
	}

	public void onMessage(E m, EntityPlayer player)
	{
	}
}