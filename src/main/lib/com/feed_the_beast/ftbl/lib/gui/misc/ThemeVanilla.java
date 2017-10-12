package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.gui.Theme;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.ColoredIcon;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.icon.PartIcon;

/**
 * @author LatvianModder
 */
public class ThemeVanilla extends Theme
{
	public static final ThemeVanilla INSTANCE = new ThemeVanilla();
	private static final Color4I COLOR_DARK = Color4I.rgb(0x404040);
	private static final Icon TEXTURE_BEACON = Icon.getIcon("textures/gui/container/beacon.png");
	private static final Icon TEXTURE_WIDGETS = Icon.getIcon("textures/gui/widgets.png");
	private static final Icon TEXTURE_RECIPE_BOOK = Icon.getIcon("textures/gui/recipe_book.png");
	private static final Icon TEXTURE_ADV_WIDGETS = Icon.getIcon("textures/gui/advancements/widgets.png");

	private static final Icon GUI = new PartIcon(TEXTURE_RECIPE_BOOK, 82, 208, 256, 256, 8, 16, 16, true);
	private static final Icon GUI_MOUSE_OVER = new ColoredIcon(GUI, Color4I.rgb(0xAFB6DA), 0);
	private static final Icon BUTTON = new PartIcon(TEXTURE_WIDGETS, 0, 66, 256, 256, 4, 192, 12, false);
	private static final Icon DISABLED_BUTTON = new PartIcon(TEXTURE_WIDGETS, 0, 46, 256, 256, 4, 192, 12, false);
	private static final Icon BUTTON_MOUSE_OVER = new PartIcon(TEXTURE_WIDGETS, 0, 86, 256, 256, 4, 192, 12, false);
	private static final Icon WIDGET = new PartIcon(TEXTURE_BEACON, 0, 219, 256, 256, 6, 10, 10, true);
	private static final Icon DISABLED_WIDGET = new PartIcon(TEXTURE_BEACON, 44, 219, 256, 256, 6, 10, 10, true);
	private static final Icon WIDGET_MOUSE_OVER = new PartIcon(TEXTURE_BEACON, 66, 219, 256, 256, 6, 10, 10, true);
	private static final Icon SLOT = new PartIcon(TEXTURE_BEACON, 35, 136, 256, 256, 6, 6, 6, true);
	private static final Icon SLOT_MOUSE_OVER = SLOT.combineWith(Color4I.WHITE_A[33]);
	private static final Icon SCROLL_BAR = new ColoredIcon(WIDGET, Icon.EMPTY, 1);
	private static final Icon SCROLL_BAR_GRABBED = new ColoredIcon(WIDGET_MOUSE_OVER, Icon.EMPTY, 1);
	private static final Icon TEXT_BOX = new PartIcon(TEXTURE_ADV_WIDGETS, 0, 55, 256, 256, 4, 192, 12, false);
	private static final Icon PANEL_BACKGROUND = new ColoredIcon(SLOT, Icon.EMPTY, -1);

	@Override
	public Color4I getContentColor()
	{
		return Color4I.WHITE;
	}

	@Override
	public Icon getGui(boolean mouseOver)
	{
		return mouseOver ? GUI_MOUSE_OVER : GUI;
	}

	@Override
	public Icon getWidget(boolean mouseOver)
	{
		return mouseOver ? WIDGET_MOUSE_OVER : WIDGET;
	}

	@Override
	public Icon getDisabledWidget()
	{
		return DISABLED_WIDGET;
	}

	@Override
	public Icon getSlot(boolean mouseOver)
	{
		return mouseOver ? SLOT_MOUSE_OVER : SLOT;
	}

	@Override
	public Icon getButton(boolean mouseOver)
	{
		return mouseOver ? BUTTON_MOUSE_OVER : BUTTON;
	}

	@Override
	public Icon getDisabledButton()
	{
		return DISABLED_BUTTON;
	}

	@Override
	public Icon getScrollBar(boolean grabbed, boolean vertical)
	{
		return grabbed ? SCROLL_BAR_GRABBED : SCROLL_BAR;
	}

	@Override
	public Icon getTextBox()
	{
		return TEXT_BOX;
	}

	@Override
	public Icon getCheckboxBackground(boolean radioButton)
	{
		return getSlot(false);
	}

	@Override
	public Icon getCheckbox(boolean mouseOver, boolean selected, boolean radioButton)
	{
		return selected ? getWidget(mouseOver) : Icon.EMPTY;
	}

	@Override
	public Icon getPanelBackground()
	{
		return PANEL_BACKGROUND;
	}
}