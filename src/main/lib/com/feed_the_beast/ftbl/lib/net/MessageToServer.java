package com.feed_the_beast.ftbl.lib.net;

import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by LatvianModder on 14.05.2016.
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