package com.feed_the_beast.ftbl.api.net;

import com.latmod.lib.util.LMUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by LatvianModder on 14.05.2016.
 */
public abstract class MessageToClient<E extends MessageToClient<E>> extends MessageLM<E>
{
    @Override
    final Side getReceivingSide()
    {
        return Side.CLIENT;
    }

    @Override
    @Nullable
    public final IMessage onMessage(final E m, MessageContext ctx)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> onMessage(m));

        if(MessageLM.LOG_NET)
        {
            LMUtils.DEV_LOGGER.info("RX MessageLM: " + getClass().getName());
        }

        return null;
    }

    public void onMessage(E m)
    {
    }

    public final void sendTo(@Nullable EntityPlayerMP player)
    {
        if(player != null)
        {
            getWrapper().sendTo(this, player);
        }
        else
        {
            getWrapper().sendToAll(this);
        }
    }

    public final void sendToDimension(DimensionType dim)
    {
        getWrapper().sendToDimension(this, dim);
    }
}