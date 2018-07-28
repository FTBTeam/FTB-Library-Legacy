package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

import java.util.List;

/**
 * @author LatvianModder
 */
public class ContextMenu extends Panel
{
	public static class CButton extends Button
	{
		public final ContextMenuItem item;

		public CButton(Panel panel, ContextMenuItem i)
		{
			super(panel, i.title, i.icon);
			item = i;
			setSize(getStringWidth(item.title) + 14, 12);
		}

		@Override
		public void addMouseOverText(List<String> list)
		{
		}

		@Override
		public WidgetType getWidgetType()
		{
			return item.enabled ? super.getWidgetType() : WidgetType.DISABLED;
		}

		@Override
		public void draw()
		{
			int x = getAX();
			int y = getAY();
			getIcon().draw(x + 1, y + 2, 8, 8);
			drawString(getTitle(), x + 11, y + 2, getTheme().getContentColor(getWidgetType()), SHADOW);
		}

		@Override
		public void onClicked(MouseButton button)
		{
			GuiHelper.playClickSound();

			if (item.yesNoText.isEmpty())
			{
				item.callback.run();
			}
			else
			{
				getGui().openYesNo(item.yesNoText, "", item.callback);
			}
		}
	}

	public static class CSeperator extends Widget
	{
		public CSeperator(Panel panel)
		{
			super(panel);
			setHeight(5);
		}

		@Override
		public void draw()
		{
			int x = getAX();
			int y = getAY();
			Color4I.WHITE.withAlpha(130).draw(x + 2, y + 2, parent.width - 4, 1);
		}
	}

	private final List<ContextMenuItem> items;
	public Icon background;

	public ContextMenu(Panel panel, List<ContextMenuItem> i)
	{
		super(panel);
		items = i;
		background = getTheme().getGui(WidgetType.NORMAL).withBorder(-3).withTint(Color4I.BLACK.withAlpha(90));
	}

	@Override
	public void addWidgets()
	{
		for (ContextMenuItem item : items)
		{
			add(item.createWidget(this));
		}
	}

	@Override
	public Icon getIcon()
	{
		return background;
	}

	@Override
	public void alignWidgets()
	{
		setWidth(0);

		for (Widget widget : widgets)
		{
			setWidth(Math.max(width, widget.width));
		}

		for (Widget widget : widgets)
		{
			widget.setWidth(width);
		}

		setHeight(align(WidgetLayout.VERTICAL));
	}
}