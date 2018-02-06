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
		panelButtons = new Panel(gui)
		{
			@Override
			public void addWidgets()
			{
				addButtons(this);
			}

			@Override
			public void alignWidgets()
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

				int size = align(WidgetLayout.VERTICAL);

				setHeight(Math.min(size, 146));

				scrollBar.setPosAndSize(posX + width + 6, 8, 16, height + 2);
				scrollBar.setElementSize(size);
				scrollBar.setSrollStepFromOneElementSize(20);

				gui.setWidth(scrollBar.posX + scrollBar.width + 8);
				gui.setHeight(height + 18);
			}

			@Override
			public Icon getIcon()
			{
				return gui.getTheme().getPanelBackground();
			}
		};

		panelButtons.setPosAndSize(9, 9, 144, 146);
		panelButtons.addFlags(Panel.DEFAULTS);

		scrollBar = new PanelScrollBar(this, panelButtons)
		{
			@Override
			public boolean shouldDraw()
			{
				return true;
			}

			@Override
			public boolean canMouseScroll()
			{
				return true;
			}
		};
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
		String title = getTitle();

		if (!title.isEmpty())
		{
			drawString(title, getAX() + (width - gui.getStringWidth(title)) / 2, getAY() - getFontHeight() - 2, SHADOW);
		}
	}
}