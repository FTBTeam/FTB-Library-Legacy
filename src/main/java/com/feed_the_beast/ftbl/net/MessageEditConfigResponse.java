package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class MessageEditConfigResponse extends MessageToServer<MessageEditConfigResponse>
{
	private JsonObject groupData;
	private NBTTagCompound extraNBT;

	public MessageEditConfigResponse()
	{
	}

	public MessageEditConfigResponse(@Nullable NBTTagCompound nbt, JsonObject json)
	{
		groupData = json;
		extraNBT = nbt;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.NET;
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		groupData = NetUtils.readJsonElement(io).getAsJsonObject();
		extraNBT = ByteBufUtils.readTag(io);
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		NetUtils.writeJsonElement(io, groupData);
		ByteBufUtils.writeTag(io, extraNBT);
	}

	@Override
	public void onMessage(MessageEditConfigResponse m, EntityPlayer player)
	{
		FTBLibModCommon.EditingConfig c = FTBLibModCommon.TEMP_SERVER_CONFIG.get(player.getGameProfile().getId());

		if (c != null)
		{
			c.callback.saveConfig(c.tree, player, m.extraNBT, m.groupData);
			FTBLibModCommon.TEMP_SERVER_CONFIG.remove(player.getGameProfile().getId());
		}
	}
}