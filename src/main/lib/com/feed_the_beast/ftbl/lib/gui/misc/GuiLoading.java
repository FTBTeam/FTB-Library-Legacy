package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.icon.LoadingIcon;

/**
 * @author LatvianModder
 */
public class GuiLoading extends GuiBase
{
	private boolean startedLoading = false;
	private boolean isLoading = true;

	public GuiLoading()
	{
		super(128, 128);
	}

	@Override
	public void addWidgets()
	{
	}

	@Override
	public void drawBackground()
	{
		if (!startedLoading)
		{
			startLoading();
			startedLoading = true;
		}

		if (isLoading())
		{
			LoadingIcon.INSTANCE.draw(this, Color4I.WHITE);
		}
		else
		{
			closeGui();
			finishLoading();
		}
	}

	public void setFinished()
	{
		isLoading = false;
	}

	public void startLoading()
	{
	}

	public boolean isLoading()
	{
		return isLoading;
	}

	public void finishLoading()
	{
	}
}