package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.config.ConfigColor;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.icon.Color4I;

//FIXME
public class GuiColorField extends GuiBase
{
	private final Color4I initCol;
	private final IGuiFieldCallback callback;

	GuiColorField(ConfigColor p, IGuiFieldCallback c)
	{
		super(256, 256);
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