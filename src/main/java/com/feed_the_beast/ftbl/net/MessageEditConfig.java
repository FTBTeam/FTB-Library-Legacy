package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.FTBLibModCommon;
import com.feed_the_beast.ftbl.api.config.IConfigContainer;
import com.feed_the_beast.ftbl.api.config.IConfigTree;
import com.feed_the_beast.ftbl.lib.config.BasicConfigContainer;
import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiEditConfig;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.CommonUtils;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author LatvianModder
 */
public class MessageEditConfig extends MessageToClient<MessageEditConfig> // MessageEditConfigResponse
{
	private static NBTTagCompound RX_NBT;
	private static final IConfigTree RX_CONFIG_TREE = new ConfigTree()
	{
		@Override
		public void fromJson(JsonElement json)
		{
			new MessageEditConfigResponse(RX_NBT, json.getAsJsonObject()).sendToServer();
		}
	};

	private IConfigTree group;
	private NBTTagCompound extraNBT;
	private ITextComponent title;

	public MessageEditConfig()
	{
	}

	public MessageEditConfig(UUID id, @Nullable NBTTagCompound nbt, IConfigContainer c)
	{
		FTBLibModCommon.TEMP_SERVER_CONFIG.put(id, c);
		group = c.getConfigTree().copy();
		extraNBT = nbt;
		title = c.getTitle();

		if (CommonUtils.DEV_ENV)
		{
			CommonUtils.DEV_LOGGER.info("TX Send: " + group.getTree());
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
		RX_NBT = ByteBufUtils.readTag(io);
		title = NetUtils.readTextComponent(io);
		RX_CONFIG_TREE.readData(io);
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		ByteBufUtils.writeTag(io, extraNBT);
		NetUtils.writeTextComponent(io, title);
		group.writeData(io);
	}

	@Override
	public void onMessage(final MessageEditConfig m, EntityPlayer player)
	{
		if (CommonUtils.DEV_ENV)
		{
			CommonUtils.DEV_LOGGER.info("RX Send: " + RX_CONFIG_TREE.getTree());
		}

		new GuiEditConfig(RX_NBT, new BasicConfigContainer(m.title, RX_CONFIG_TREE)).openGui();
	}
}