package com.feed_the_beast.ftbl.api.net;

import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

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
    public final IMessage onMessage(E m, MessageContext ctx)
    {
        EntityPlayerMP ep = ctx.getServerHandler().playerEntity;
        ep.mcServer.addScheduledTask(() -> onMessage(m, ep));

        if(MessageLM.logMessages())
        {
            FTBLib.dev_logger.info("TX MessageLM: " + getClass().getName());
        }

        return null;
    }

    public void onMessage(E m, EntityPlayerMP ep)
    {
    }

    public final void sendToServer()
    {
        getWrapper().sendToServer(this);
    }
}