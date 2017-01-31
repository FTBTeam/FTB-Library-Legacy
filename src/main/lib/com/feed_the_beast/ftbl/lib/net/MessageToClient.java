package com.feed_the_beast.ftbl.lib.net;

import com.feed_the_beast.ftbl.lib.internal.FTBLibIntegrationInternal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
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
        FTBLibIntegrationInternal.API.handleMessage(m, ctx, Side.CLIENT);
        return null;
    }

    public final void sendTo(@Nullable EntityPlayer player)
    {
        if(player != null)
        {
            getWrapper().sendTo(this, (EntityPlayerMP) player);
        }
        else
        {
            getWrapper().sendToAll(this);
        }
    }

    public final void sendToDimension(int dim)
    {
        getWrapper().sendToDimension(this, dim);
    }

    public final void sendToAllAround(int dim, BlockPos pos, double range)
    {
        getWrapper().sendToAllAround(this, new NetworkRegistry.TargetPoint(dim, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, range));
    }
}