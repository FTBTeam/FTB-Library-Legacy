package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import com.feed_the_beast.ftbl.lib.icon.Icon;

/**
 * @author LatvianModder
 */
public class GuiButtonListBase extends GuiBase
{
	private final Panel panelButtons;
	private final PanelScrollBar scrollBar;
	private String title = "";

	public GuiButtonListBase()
	{
		super(0, 0);

		panelButtons = new Panel(gui, 9, 9, 0, 146)
		{
			@Override
			public void addWidgets()
			{
				width = 0;

				addButtons(this);

				for (Widget w : widgets)
				{
					setWidth(Math.max(width, w.width));
				}

				for (Widget w : widgets)
				{
					w.setWidth(width);
				}

				updateWidgetPositions();
			}

			@Override
			public void updateWidgetPositions()
			{
				int size = align(WidgetLayout.VERTICAL);
				scrollBar.setElementSize(size);
				scrollBar.setSrollStepFromOneElementSize(20);
				setHeight(widgets.size() > 7 ? 144 : size);
				gui.setHeight(height + 20);
			}

			@Override
			public Icon getIcon()
			{
				return gui.getTheme().getPanelBackground();
			}
		};

		panelButtons.addFlags(Panel.DEFAULTS);

		scrollBar = new PanelScrollBar(this, 0, 8, 16, 148, 0, panelButtons)
		{
			@Override
			public boolean shouldRender()
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

		if (panelButtons.widgets.size() > 7)
		{
			add(scrollBar);
		}

		scrollBar.setX(panelButtons.posX + panelButtons.width + 6);
		setWidth(scrollBar.posX + (panelButtons.widgets.size() > 7 ? scrollBar.width + 8 : 4));
		posX = (getScreen().getScaledWidth() - width) / 2;
	}

	public void addButtons(Panel panel)
	{
	}

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