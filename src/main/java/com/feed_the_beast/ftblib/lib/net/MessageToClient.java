package com.feed_the_beast.ftblib.lib.net;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * @author LatvianModder
 */
public abstract class MessageToClient<E extends MessageToClient<E>> extends MessageBase<E>
{
	@Override
	final Side getReceivingSide()
	{
		return Side.CLIENT;
	}

	public final void sendTo(EntityPlayer player)
	{
		getWrapper().sendTo(this, (EntityPlayerMP) player);
	}

	public final void sendToAll()
	{
		getWrapper().sendToAll(this);
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