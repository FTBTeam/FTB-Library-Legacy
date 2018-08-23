package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.icon.ImageIcon;
import com.feed_the_beast.ftblib.lib.icon.PartIcon;

/**
 * @author LatvianModder
 */
public class Theme
{
	public static final Theme DEFAULT = new Theme();

	private static final Color4I CONTENT_COLOR_MOUSE_OVER = Color4I.rgb(16777120);
	private static final Color4I CONTENT_COLOR_DISABLED = Color4I.rgb(10526880);
	private static final Color4I CONTENT_COLOR_DARK = Color4I.rgb(4210752);

	private static final ImageIcon TEXTURE_BEACON = (ImageIcon) Icon.getIcon("textures/gui/container/beacon.png");
	private static final ImageIcon TEXTURE_WIDGETS = (ImageIcon) Icon.getIcon("textures/gui/widgets.png");
	private static final ImageIcon TEXTURE_RECIPE_BOOK = (ImageIcon) Icon.getIcon("textures/gui/recipe_book.png");
	private static final ImageIcon TEXTURE_ENCHANTING_TABLE = (ImageIcon) Icon.getIcon("textures/gui/container/enchanting_table.png");

	private static final Icon GUI = new PartIcon(TEXTURE_RECIPE_BOOK, 82, 208, 256, 256, 8, 16, 16, true);
	private static final Icon GUI_MOUSE_OVER = GUI.withTint(Color4I.rgb(0xAFB6DA));

	private static final Icon BUTTON = new PartIcon(TEXTURE_WIDGETS, 0, 66, 256, 256, 4, 192, 12, false);
	private static final Icon BUTTON_MOUSE_OVER = new PartIcon(TEXTURE_WIDGETS, 0, 86, 256, 256, 4, 192, 12, false);
	private static final Icon BUTTON_DISABLED = new PartIcon(TEXTURE_WIDGETS, 0, 46, 256, 256, 4, 192, 12, false);

	private static final Icon WIDGET = new PartIcon(TEXTURE_BEACON, 0, 219, 256, 256, 6, 10, 10, true);
	private static final Icon WIDGET_MOUSE_OVER = new PartIcon(TEXTURE_BEACON, 66, 219, 256, 256, 6, 10, 10, true);
	private static final Icon WIDGET_DISABLED = new PartIcon(TEXTURE_BEACON, 44, 219, 256, 256, 6, 10, 10, true);

	private static final Icon SLOT = new PartIcon(TEXTURE_BEACON, 35, 136, 256, 256, 3, 12, 12, true);
	private static final Icon SLOT_MOUSE_OVER = SLOT.combineWith(Color4I.WHITE.withAlpha(33));

	private static final Icon SCROLL_BAR = WIDGET.withBorder(1);
	private static final Icon SCROLL_BAR_MOUSE_OVER = WIDGET_MOUSE_OVER.withBorder(1);
	private static final Icon SCROLL_BAR_BG = SLOT;
	private static final Icon SCROLL_BAR_BG_DISABLED = SCROLL_BAR_BG.withTint(Color4I.BLACK.withAlpha(100));

	private static final Icon TEXT_BOX = new PartIcon(TEXTURE_ENCHANTING_TABLE, 0, 185, 256, 256, 6, 96, 7, false);

	private static final Icon CONTAINER_SLOT = SLOT.withBorder(-1);

	private static final Icon TAB_H_UNSELECTED = TEXTURE_RECIPE_BOOK.withUVfromCoords(150, 2, 35, 26, 256, 256);
	private static final Icon TAB_H_SELECTED = TEXTURE_RECIPE_BOOK.withUVfromCoords(188, 2, 35, 26, 256, 256);

	public Color4I getContentColor(WidgetType type)
	{
		return type == WidgetType.MOUSE_OVER ? CONTENT_COLOR_MOUSE_OVER : type == WidgetType.DISABLED ? CONTENT_COLOR_DISABLED : Color4I.WHITE;
	}

	public Color4I getInvertedContentColor()
	{
		return CONTENT_COLOR_DARK;
	}

	public Icon getGui(WidgetType type)
	{
		return type == WidgetType.MOUSE_OVER ? GUI_MOUSE_OVER : GUI;
	}

	public Icon getWidget(WidgetType type)
	{
		return type == WidgetType.MOUSE_OVER ? WIDGET_MOUSE_OVER : type == WidgetType.DISABLED ? WIDGET_DISABLED : WIDGET;
	}

	public Icon getSlot(WidgetType type)
	{
		return type == WidgetType.MOUSE_OVER ? SLOT_MOUSE_OVER : SLOT;
	}

	public Icon getContainerSlot()
	{
		return CONTAINER_SLOT;
	}

	public Icon getButton(WidgetType type)
	{
		return type == WidgetType.MOUSE_OVER ? BUTTON_MOUSE_OVER : type == WidgetType.DISABLED ? BUTTON_DISABLED : BUTTON;
	}

	public Icon getScrollBarBackground(WidgetType type)
	{
		return type == WidgetType.DISABLED ? SCROLL_BAR_BG_DISABLED : SCROLL_BAR_BG;
	}

	public Icon getScrollBar(WidgetType type, boolean vertical)
	{
		return type == WidgetType.MOUSE_OVER ? SCROLL_BAR_MOUSE_OVER : SCROLL_BAR;
	}

	public Icon getTextBox()
	{
		return TEXT_BOX;
	}

	public Icon getCheckboxBackground(boolean radioButton)
	{
		return getSlot(WidgetType.NORMAL);
	}

	public Icon getCheckbox(WidgetType type, boolean selected, boolean radioButton)
	{
		return selected ? getWidget(type) : Icon.EMPTY;
	}

	public Icon getPanelBackground()
	{
		return getContainerSlot();
	}

	public Icon getHorizontalTab(boolean selected)
	{
		return selected ? TAB_H_SELECTED : TAB_H_UNSELECTED;
	}

	public Icon getContextMenuBackground()
	{
		return getGui(WidgetType.NORMAL).withTint(Color4I.BLACK.withAlpha(90));
	}
}