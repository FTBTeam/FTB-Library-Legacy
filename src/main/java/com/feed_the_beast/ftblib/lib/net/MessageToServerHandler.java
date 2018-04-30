package com.feed_the_beast.ftblib.lib.net;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.FTBLibConfig;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * @author LatvianModder
 */
enum MessageToServerHandler implements IMessageHandler<MessageToServer, IMessage>
{
	INSTANCE;

	@Override
	public IMessage onMessage(MessageToServer message, MessageContext context)
	{
		FMLCommonHandler.instance().getWorldThread(context.netHandler).addScheduledTask(() ->
		{
			if (FTBLibConfig.debugging.log_network)
			{
				FTBLib.LOGGER.info("Net TX: " + message.getClass().getName());
			}

			message.onMessage(context.getServerHandler().player);
		});

		return null;
	}
}