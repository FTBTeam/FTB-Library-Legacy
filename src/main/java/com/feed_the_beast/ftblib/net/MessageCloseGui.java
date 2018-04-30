package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.lib.client.ClientUtils;
import com.feed_the_beast.ftblib.lib.gui.IGuiWrapper;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author LatvianModder
 */
public class MessageCloseGui extends MessageToClient
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
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		if (ClientUtils.MC.currentScreen instanceof IGuiWrapper)
		{
			((IGuiWrapper) ClientUtils.MC.currentScreen).getGui().closeGui();
		}
		else if (!(ClientUtils.MC.currentScreen instanceof GuiChat))
		{
			ClientUtils.MC.player.closeScreen();
		}
	}
}