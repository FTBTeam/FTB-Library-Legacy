package com.feed_the_beast.ftbl.lib.net;

import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public abstract class MessageLM<E extends MessageLM<E>> implements IMessage, IMessageHandler<E, IMessage>
{
    MessageLM()
    {
    }

    public abstract LMNetworkWrapper getWrapper();

    abstract Side getReceivingSide();

    @Override
    public final IMessage onMessage(final E m, MessageContext ctx)
    {
        FTBLibIntegrationInternal.API.handleMessage(m, ctx, getReceivingSide());
        return null;
    }

    public void onMessage(E m, EntityPlayer player)
    {
    }
}