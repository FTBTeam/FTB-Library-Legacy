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

				if (scrollToEnd())
				{
					scrollBar.setValue(1D);
				}
			}

			@Override
			public void updateWidgetPositions()
			{
				scrollBar.setElementSize(align(WidgetLayout.VERTICAL));
				scrollBar.setSrollStepFromOneElementSize(20);
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

		setHeight(164);
	}

	@Override
	public void addWidgets()
	{
		addAll(panelButtons, scrollBar);
		scrollBar.setX(panelButtons.posX + panelButtons.width + 6);
		setWidth(scrollBar.posX + scrollBar.width + 8);
		posX = (getScreen().getScaledWidth() - width) / 2;
	}

	public boolean scrollToEnd()
	{
		return false;
	}

	public void addButtons(Panel panel)
	{
	}
}