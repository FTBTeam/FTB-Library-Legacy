package com.feed_the_beast.ftbl.net;

import com.feed_the_beast.ftbl.lib.client.ClientUtils;
import com.feed_the_beast.ftbl.lib.gui.IGuiWrapper;
import com.feed_the_beast.ftbl.lib.net.MessageToClient;
import com.feed_the_beast.ftbl.lib.net.NetworkWrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.entity.player.EntityPlayer;

/**
 * @author LatvianModder
 */
public class MessageCloseGui extends MessageToClient<MessageCloseGui>
{
	@Override
	public NetworkWrapper getWrapper()
	{
		return FTBLibNetHandler.GENERAL;
	}

	@Override
	public boolean hasData()
	{
		return false;
	}

	@Override
	public void onMessage(MessageCloseGui m, EntityPlayer player)
	{
		if (ClientUtils.MC.currentScreen instanceof IGuiWrapper)
		{
			((IGuiWrapper) ClientUtils.MC.currentScreen).getWrappedGui().closeGui();
		}
		else if (!(ClientUtils.MC.currentScreen instanceof GuiChat))
		{
			ClientUtils.MC.player.closeScreen();
		}
	}
}