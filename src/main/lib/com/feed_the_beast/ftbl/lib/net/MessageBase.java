package com.feed_the_beast.ftbl.lib.net;

import com.feed_the_beast.ftbl.api.FTBLibAPI;
import com.feed_the_beast.ftbl.lib.io.DataIn;
import com.feed_the_beast.ftbl.lib.io.DataOut;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

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
	public final IMessage onMessage(final E m, MessageContext ctx)
	{
		FTBLibAPI.API.handleMessage(m, ctx, getReceivingSide());
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