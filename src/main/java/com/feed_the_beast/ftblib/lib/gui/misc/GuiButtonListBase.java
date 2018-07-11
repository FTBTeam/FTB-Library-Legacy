package com.feed_the_beast.ftblib.lib.gui.misc;

import com.feed_the_beast.ftblib.lib.gui.GuiBase;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.PanelScrollBar;
import com.feed_the_beast.ftblib.lib.gui.TextBox;
import com.feed_the_beast.ftblib.lib.gui.Widget;
import com.feed_the_beast.ftblib.lib.gui.WidgetLayout;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import net.minecraft.client.resources.I18n;

/**
 * @author LatvianModder
 */
public abstract class GuiButtonListBase extends GuiBase
{
	private final Panel panelButtons;
	private final PanelScrollBar scrollBar;
	private String title = "";
	private TextBox textBox;
	private boolean hasSearchBox;

	public GuiButtonListBase()
	{
		panelButtons = new Panel(this)
		{
			@Override
			public void add(Widget widget)
			{
				if (!hasSearchBox || getFilterText(widget).toLowerCase().contains(textBox.getText().toLowerCase()))
				{
					super.add(widget);
				}
			}

			@Override
			public void addWidgets()
			{
				addButtons(this);
			}

			@Override
			public void alignWidgets()
			{
				int size = 0;
				setY(hasSearchBox ? 23 : 9);
				int prevWidth = width;

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
				}

				if (hasSearchBox)
				{
					setWidth(Math.max(width, prevWidth));
				}

				for (Widget w : widgets)
				{
					w.setWidth(width);
				}

				setHeight(140);

				scrollBar.setPosAndSize(posX + width + 6, posY - 1, 16, height + 2);
				scrollBar.setMaxValue(align(WidgetLayout.VERTICAL));

				getGui().setWidth(scrollBar.posX + scrollBar.width + 8);
				getGui().setHeight(height + 18 + (hasSearchBox ? 14 : 0));

				if (hasSearchBox)
				{
					textBox.setPosAndSize(8, 6, getGui().width - 16, 12);
				}
			}

			@Override
			public Icon getIcon()
			{
				return getTheme().getPanelBackground();
			}
		};

		panelButtons.setPosAndSize(9, 9, 0, 146);

		scrollBar = new PanelScrollBar(this, panelButtons);
		scrollBar.setCanAlwaysScroll(true);
		scrollBar.setScrollStep(20);

		textBox = new TextBox(this)
		{
			@Override
			public void onTextChanged()
			{
				panelButtons.refreshWidgets();
			}
		};

		textBox.ghostText = I18n.format("gui.search_box");
		hasSearchBox = false;
	}

	public void setHasSearchBox(boolean v)
	{
		if (hasSearchBox != v)
		{
			hasSearchBox = v;
			refreshWidgets();
		}
	}

	public String getFilterText(Widget widget)
	{
		return widget.getTitle();
	}

	@Override
	public void addWidgets()
	{
		add(panelButtons);
		add(scrollBar);

		if (hasSearchBox)
		{
			add(textBox);
		}
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