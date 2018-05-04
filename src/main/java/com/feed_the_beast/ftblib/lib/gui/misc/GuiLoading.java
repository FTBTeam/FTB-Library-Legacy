package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.LoadingIcon;

/**
 * @author LatvianModder
 */
public class GuiLoading extends GuiBase
{
	private boolean startedLoading = false;
	private boolean isLoading = true;
	private String title = "";

	public GuiLoading()
	{
	}

	public GuiLoading(String t)
	{
		title = t;
	}

	@Override
	public void addWidgets()
	{
	}

	@Override
	public void alignWidgets()
	{
		setSize(128, 128);
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
			int ax = getAX();
			int ay = getAY();
			LoadingIcon.INSTANCE.draw(ax, ay, width, height);
			String s = getTitle();

			if (!s.isEmpty())
			{
				String[] s1 = s.split("\n");

				for (int i = 0; i < s1.length; i++)
				{
					drawString(s1[i], ax + width / 2, ay - 26 + i * 12, CENTERED);
				}
			}
		}
		else
		{
			closeGui();
			finishLoading();
		}
	}

	@Override
	public synchronized String getTitle()
	{
		return title;
	}

	public synchronized void setTitle(String s)
	{
		title = s;
	}

	public synchronized void setFinished()
	{
		isLoading = false;
	}

	public void startLoading()
	{
	}

	public synchronized boolean isLoading()
	{
		return isLoading;
	}

	public void finishLoading()
	{
	}

	@Override
	public Icon getIcon()
	{
		return Icon.EMPTY;
	}
}