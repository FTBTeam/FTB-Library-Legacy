package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.client.GuiAdminPanel;
import com.feed_the_beast.ftblib.lib.data.Action;
import com.feed_the_beast.ftblib.lib.io.DataIn;
import com.feed_the_beast.ftblib.lib.io.DataOut;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collection;

/**
 * @author LatvianModder
 */
public class MessageAdminPanelGuiResponse extends MessageToClient
{
	private Collection<Action.Inst> actions;

	public MessageAdminPanelGuiResponse()
	{
	}

	public MessageAdminPanelGuiResponse(Collection<Action.Inst> a)
	{
		actions = a;
	}

	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.GENERAL;
	}

	@Override
	public void writeData(DataOut data)
	{
		data.writeCollection(actions, Action.Inst.SERIALIZER);
	}

	@Override
	public void readData(DataIn data)
	{
		actions = data.readCollection(Action.Inst.DESERIALIZER);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		new GuiAdminPanel(actions).openGui();
	}
}