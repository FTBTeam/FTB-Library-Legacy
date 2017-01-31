package com.feed_the_beast.ftbl.lib.net;

import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public abstract class MessageToServer<E extends MessageToServer<E>> extends MessageLM<E>
{
    @Override
    final Side getReceivingSide()
    {
        return Side.SERVER;
    }

    @Override
    @Nullable
    public IMessage onMessage(final E m, MessageContext ctx)
    {
        FTBLibIntegrationInternal.API.handleMessage(m, ctx, Side.SERVER);
        return null;
    }

    public final void sendToServer()
    {
        getWrapper().sendToServer(this);
    }
}