package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.lib.config.ConfigGroup;
import com.feed_the_beast.ftblib.lib.config.IConfigCallback;
import com.feed_the_beast.ftblib.lib.gui.misc.GuiEditConfig;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class MessageEditConfig extends MessageToClient
{
	private static final IConfigCallback RX_CONFIG_TREE = (group, sender) -> new MessageEditConfigResponse(group.serializeNBT()).sendToServer();

	private ConfigGroup group;

	public MessageEditConfig()
	{
	}

	public MessageEditConfig(ConfigGroup _group)
	{
		group = _group;
		//TODO: Logger
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.EDIT_CONFIG;
	}

	@Override
	public void writeData(DataOut data)
	{
		ConfigGroup.SERIALIZER.write(data, group);
	}

	@Override
	public void readData(DataIn data)
	{
		group = ConfigGroup.DESERIALIZER.read(data);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		new GuiEditConfig(group, RX_CONFIG_TREE).openGui();
	}
}