package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.api.gui.IGuiProvider;
import com.feed_the_beast.ftbl.client.FTBLibModClient;
import com.feed_the_beast.ftbl.lib.client.FTBLibClient;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class MessageOpenGui extends MessageToClient<MessageOpenGui>
{
	private ResourceLocation guiID;
	private BlockPos pos;
	private NBTTagCompound data;
	private int windowID;

	public MessageOpenGui()
	{
	}

	public MessageOpenGui(ResourceLocation key, BlockPos p, @Nullable NBTTagCompound tag, int wid)
	{
		guiID = key;
		pos = p;
		data = tag;
		windowID = wid;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.NET;
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		guiID = NetUtils.readResourceLocation(io);
		pos = NetUtils.readPos(io);
		data = ByteBufUtils.readTag(io);
		windowID = io.readUnsignedByte();
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		NetUtils.writeResourceLocation(io, guiID);
		NetUtils.writePos(io, pos);
		ByteBufUtils.writeTag(io, data);
		io.writeByte(windowID);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage(MessageOpenGui m, EntityPlayer player)
	{
		IGuiProvider guiProvider = FTBLibModClient.getGui(m.guiID);

		if (guiProvider != null)
		{
			GuiScreen g = guiProvider.getGui(player, m.pos, m.data);

			if (g != null)
			{
				FTBLibClient.MC.displayGuiScreen(g);
				player.openContainer.windowId = m.windowID;
			}
		}
	}
}