package com.feed_the_beast.ftblib.net;

import com.feed_the_beast.ftblib.lib.gui.IGuiWrapper;
import com.feed_the_beast.ftblib.lib.net.MessageToClient;
import com.feed_the_beast.ftblib.lib.net.NetworkWrapper;
import net.minecraft.client.Minecraft;
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
	@SideOnly(Side.CLIENT)
	public void onMessage()
	{
		Minecraft mc = Minecraft.getMinecraft();

		if (mc.currentScreen instanceof IGuiWrapper)
		{
			((IGuiWrapper) mc.currentScreen).getGui().closeGui();
		}
		else if (!(mc.currentScreen instanceof GuiChat))
		{
			mc.player.closeScreen();
		}
	}
}