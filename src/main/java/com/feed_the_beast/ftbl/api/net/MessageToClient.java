package com.feed_the_beast.ftbl.api.net;

import com.feed_the_beast.ftbl.api.client.FTBLibClient;
import com.feed_the_beast.ftbl.util.FTBLib;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.DimensionType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public final IMessage onMessage(E m, MessageContext ctx)
    {
        Minecraft mc = FTBLibClient.mc();
        mc.addScheduledTask(() -> onMessage(m, mc));

        if(MessageLM.logMessages())
        {
            FTBLib.dev_logger.info("RX MessageLM: " + getClass().getName());
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public void onMessage(E m, Minecraft mc)
    {
    }

    public final void sendTo(EntityPlayerMP ep)
    {
        if(ep != null)
        {
            getWrapper().sendTo(this, ep);
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