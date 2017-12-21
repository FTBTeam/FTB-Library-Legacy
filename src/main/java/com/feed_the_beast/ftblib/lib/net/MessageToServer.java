package com.feed_the_beast.ftblib.lib.net;

import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public abstract class MessageToServer<E extends MessageToServer<E>> extends MessageBase<E>
{
	@Override
	final Side getReceivingSide()
	{
		return Side.SERVER;
	}

	public final void sendToServer()
	{
		getWrapper().sendToServer(this);
	}
}