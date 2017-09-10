package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.config.ConfigGroup;
import com.feed_the_beast.ftbl.lib.config.IConfigCallback;
import com.feed_the_beast.ftbl.lib.gui.misc.GuiEditConfig;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author LatvianModder
 */
public class MessageEditConfig extends MessageToClient<MessageEditConfig>
{
	private static final IConfigCallback RX_CONFIG_TREE = (group, sender, json) -> new MessageEditConfigResponse(json.getAsJsonObject()).sendToServer();

	private ConfigGroup group;

	public MessageEditConfig()
	{
	}

	public MessageEditConfig(ConfigGroup _group)
	{
		group = _group;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.NET;
	}

	@Override
	public void fromBytes(ByteBuf io)
	{
		group = new ConfigGroup(null);
		group.readData(io);
	}

	@Override
	public void toBytes(ByteBuf io)
	{
		group.writeData(io);
	}

	@Override
	public void onMessage(final MessageEditConfig m, EntityPlayer player)
	{
		new GuiEditConfig(m.group, RX_CONFIG_TREE).openGui();
	}
}