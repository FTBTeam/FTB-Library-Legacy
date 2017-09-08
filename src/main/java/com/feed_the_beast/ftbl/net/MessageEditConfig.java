package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.config.ConfigTree;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiEditConfig;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import com.feed_the_beast.ftbl.lib.util.NetUtils;
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
	private static final IConfigCallback RX_CONFIG_TREE = (tree, sender, nbt, json) -> new MessageEditConfigResponse(RX_NBT, json.getAsJsonObject()).sendToServer();

	private ConfigTree tree;
	private NBTTagCompound extraNBT;
	private ITextComponent title;

	public MessageEditConfig()
	{
	}

	public MessageEditConfig(UUID id, @Nullable NBTTagCompound _data, ConfigTree _tree, ITextComponent _title)
	{
		tree = _tree;
		extraNBT = _data;
		title = _title;
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
		tree = new ConfigTree();
		tree.readData(io);
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		ByteBufUtils.writeTag(io, extraNBT);
		NetUtils.writeTextComponent(io, title);
		tree.writeData(io);
	}

	@Override
	public void onMessage(final MessageEditConfig m, EntityPlayer player)
	{
		new GuiEditConfig(RX_NBT, m.tree, m.title, RX_CONFIG_TREE).openGui();
	}
}