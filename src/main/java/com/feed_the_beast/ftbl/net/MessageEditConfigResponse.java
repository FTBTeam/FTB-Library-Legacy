package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.events.ConfigLoadedEvent;
import com.feed_the_beast.ftbl.lib.net.MessageToServer;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.LoaderState;
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

		if (CommonUtils.DEV_ENV)
		{
			CommonUtils.DEV_LOGGER.info("TX Response: " + groupData);
		}
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
		IConfigContainer cc = FTBLibModCommon.TEMP_SERVER_CONFIG.get(player.getGameProfile().getId());

		if (cc != null)
		{
			if (CommonUtils.DEV_ENV)
			{
				CommonUtils.DEV_LOGGER.info("RX Response: " + m.groupData);
			}

			cc.saveConfig(player, m.extraNBT, m.groupData);
			new ConfigLoadedEvent(LoaderState.ModState.AVAILABLE).post();
			FTBLibModCommon.TEMP_SERVER_CONFIG.remove(player.getGameProfile().getId());
		}
	}
}