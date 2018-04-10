package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftblib.lib.gui.Widget;
import com.feed_the_beast.ftblib.lib.gui.WidgetLayout;
import com.feed_the_beast.ftblib.lib.icon.Icon;

/**
 * @author LatvianModder
 */
public abstract class GuiButtonListBase extends GuiBase
{
	private final Panel panelButtons;
	private final PanelScrollBar scrollBar;
	private String title = "";

	public GuiButtonListBase()
	{
		panelButtons = new Panel(this)
		{
			@Override
			public void addWidgets()
			{
				addButtons(this);
			}

			@Override
			public void alignWidgets()
			{
				int size = 0;

				if (widgets.isEmpty())
				{
					setWidth(100);
				}
				else
				{
					setWidth(0);

					for (Widget w : widgets)
					{
						setWidth(Math.max(width, w.width));
					}

					for (Widget w : widgets)
					{
						w.setWidth(width);
					}

					size = align(WidgetLayout.VERTICAL);
				}

				setHeight(140);

				scrollBar.setPosAndSize(posX + width + 6, 8, 16, height + 2);
				scrollBar.setMaxValue(size);

				getGui().setWidth(scrollBar.posX + scrollBar.width + 8);
				getGui().setHeight(height + 18);
			}

			@Override
			public Icon getIcon()
			{
				return getTheme().getPanelBackground();
			}
		};

		panelButtons.setPosAndSize(9, 9, 144, 146);

		scrollBar = new PanelScrollBar(this, panelButtons);
		scrollBar.setCanAlwaysScroll(true);
		scrollBar.setScrollStep(20);
	}

	@Override
	public void addWidgets()
	{
		add(panelButtons);
		add(scrollBar);
	}

	@Override
	public void alignWidgets()
	{
		panelButtons.alignWidgets();
	}

	public abstract void addButtons(Panel panel);

	public void setTitle(String txt)
	{
		title = txt;
	}

	@Override
	public String getTitle()
	{
		return title;
	}

	@Override
	public void drawBackground()
	{
		super.drawBackground();

		String title = getTitle();

		if (!title.isEmpty())
		{
			drawString(title, getAX() + (width - getStringWidth(title)) / 2, getAY() - getFontHeight() - 2, SHADOW);
		}
	}
}