package com.feed_the_beast.ftbl.lib.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.relauncher.Side;

public abstract class MessageLM<E extends MessageLM<E>> implements IMessage, IMessageHandler<E, IMessage>
{
    MessageLM()
    {
    }

    public abstract LMNetworkWrapper getWrapper();

    abstract Side getReceivingSide();

    public void onMessage(E m, EntityPlayer player)
    {
    }
}