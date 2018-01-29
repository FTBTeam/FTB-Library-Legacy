package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.config.ConfigColor;
import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.icon.Color4I;

//FIXME
public class GuiColorField extends GuiBase
{
	private final Color4I initCol;
	private final IGuiFieldCallback callback;

	GuiColorField(ConfigColor p, IGuiFieldCallback c)
	{
		setSize(256, 256);
		initCol = p.getColor();
		callback = c;
	}

	@Override
	public void onClosed()
	{
		callback.onCallback(new ConfigColor(initCol), false);
	}

	@Override
	public void addWidgets()
	{
	}
}