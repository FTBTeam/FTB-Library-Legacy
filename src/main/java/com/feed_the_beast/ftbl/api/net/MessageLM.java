package com.feed_the_beast.ftbl.api.net;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class MessageLM<E extends MessageLM<E>> implements IMessage, IMessageHandler<E, IMessage>
{
    static final boolean LOG_NET = System.getProperty("ftbl.logNetwork", "0").equals("1");

    MessageLM()
    {
    }

    public abstract LMNetworkWrapper getWrapper();

    abstract Side getReceivingSide();

    @Override
    public abstract void toBytes(ByteBuf io);

    @Override
    public abstract void fromBytes(ByteBuf io);

    @Override
    public IMessage onMessage(E m, MessageContext ctx)
    {
        return null;
    }
}