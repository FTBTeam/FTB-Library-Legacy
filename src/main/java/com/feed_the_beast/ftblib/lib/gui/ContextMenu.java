package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;

import java.util.List;

/**
 * @author LatvianModder
 */
public class ContextMenu extends Panel
{
	public static class CButton extends Button
	{
		public final ContextMenu contextMenu;
		public final ContextMenuItem item;

		public CButton(ContextMenu panel, ContextMenuItem i)
		{
			super(panel, i.title, i.icon);
			contextMenu = panel;
			item = i;
			setSize(panel.getGui().getTheme().getStringWidth(item.title) + (contextMenu.hasIcons ? 14 : 4), 12);
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
		public void draw(Theme theme, int x, int y, int w, int h)
		{
			if (contextMenu.hasIcons)
			{
				drawIcon(theme, x + 1, y + 2, 8, 8);
				theme.drawString(getTitle(), x + 11, y + 2, theme.getContentColor(getWidgetType()), Theme.SHADOW);
			}
			else
			{
				theme.drawString(getTitle(), x + 2, y + 2, theme.getContentColor(getWidgetType()), Theme.SHADOW);
			}
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

	public static class CSeperator extends Button
	{
		public CSeperator(Panel panel)
		{
			super(panel);
			setHeight(5);
		}

		@Override
		public void draw(Theme theme, int x, int y, int w, int h)
		{
			Color4I.WHITE.withAlpha(130).draw(x + 2, y + 2, parent.width - 10, 1);
		}

		@Override
		public void onClicked(MouseButton button)
		{
		}
	}

	private final List<ContextMenuItem> items;
	public boolean hasIcons;

	public ContextMenu(Panel panel, List<ContextMenuItem> i)
	{
		super(panel);
		items = i;
		hasIcons = false;

		for (ContextMenuItem item : items)
		{
			if (!item.icon.isEmpty())
			{
				hasIcons = true;
				break;
			}
		}
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
	public void alignWidgets()
	{
		setWidth(0);

		for (Widget widget : widgets)
		{
			setWidth(Math.max(width, widget.width));
		}

		for (Widget widget : widgets)
		{
			widget.setX(3);
			widget.setWidth(width);
		}

		setWidth(width + 6);

		setHeight(align(new WidgetLayout.Vertical(3, 1, 3)));
	}

	@Override
	public void drawBackground(Theme theme, int x, int y, int w, int h)
	{
		theme.drawContextMenuBackground(x, y, w, h);
	}
}