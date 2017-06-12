package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.config.PropertyColor;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import net.minecraft.client.renderer.GlStateManager;

//FIXME
public class GuiColorField extends GuiBase
{
	private final Color4I initCol;
	private final IGuiFieldCallback callback;

	GuiColorField(PropertyColor p, IGuiFieldCallback c)
	{
		super(256, 256);
		initCol = p.getColor();
		callback = c;
	}

	@Override
	public void onClosed()
	{
		callback.onCallback(new PropertyColor(initCol), false);
	}

	@Override
	public void addWidgets()
	{
	}

	@Override
	public void drawBackground()
	{
		GlStateManager.color(1F, 1F, 1F, 1F);
	}
}