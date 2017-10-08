package com.feed_the_beast.ftbl.lib.gui.misc;

import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Theme;
import com.feed_the_beast.ftbl.lib.icon.Color4I;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.icon.IconWithOutline;
import com.feed_the_beast.ftbl.lib.icon.MutableColor4I;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import org.lwjgl.opengl.GL11;

/**
 * @author LatvianModder
 */
public class ThemeGlass extends Theme
{
	public static final ThemeGlass INSTANCE = new ThemeGlass();
	private static final Color4I COLOR = Color4I.rgb(0xC0C0C0);
	private static final Color4I MOUSE_OVER_COLOR = Color4I.rgb(0x71C0C0);
	private static final Icon GUI = IconWithOutline.getIconWithOutline(Color4I.rgba(0xC8333333), COLOR, true);
	private static final Icon GUI_MOUSE_OVER = IconWithOutline.getIconWithOutline(Color4I.rgba(0xC8333333), MOUSE_OVER_COLOR, true);
	private static final Icon WIDGET = IconWithOutline.getIconWithOutline(Icon.EMPTY, COLOR, false);
	private static final Icon WIDGET_MOUSE_OVER = IconWithOutline.getIconWithOutline(Color4I.rgba(0xC8333333), COLOR, false);
	private static final Icon SLOT = new SlotIcon(COLOR);
	private static final Icon SLOT_MOUSE_OVER = new SlotIcon(MOUSE_OVER_COLOR);
	private static final Icon SCROLL_BAR = IconWithOutline.getIconWithOutline(Color4I.WHITE_A[33], COLOR, false);
	private static final Icon SCROLL_BAR_GRABBED = IconWithOutline.getIconWithOutline(Color4I.WHITE_A[33], MOUSE_OVER_COLOR, false);
	private static final Icon CHECKBOX = IconWithOutline.getIconWithOutline(COLOR, Color4I.WHITE_A[33], false);
	private static final Icon CHECKBOX_MOUSE_OVER = IconWithOutline.getIconWithOutline(MOUSE_OVER_COLOR, Color4I.WHITE_A[33], false);

	private static class SlotIcon extends Icon
	{
		private Color4I color, colorB, colorD;

		private SlotIcon(Color4I col)
		{
			color = col;
			MutableColor4I c = color.mutable();
			c.addBrightness(18);
			colorB = c.copy();
			c = color.mutable();
			c.addBrightness(-18);
			colorD = c.copy();
		}

		@Override
		public void draw(int x, int y, int w, int h, Color4I col)
		{
			Color4I c, cb, cd;

			if (col.isEmpty())
			{
				c = color;
				cb = colorB;
				cd = colorD;
			}
			else
			{
				c = col;
				MutableColor4I cm = col.mutable();
				cm.addBrightness(18);
				cb = cm.copy();
				cm = col.mutable();
				cm.addBrightness(-18);
				cd = cm.copy();
			}

			GlStateManager.disableTexture2D();
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder buffer = tessellator.getBuffer();
			buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);

			GuiHelper.addRectToBuffer(buffer, x, y + 1, 1, h - 2, cd);
			GuiHelper.addRectToBuffer(buffer, x + w - 1, y + 1, 1, h - 2, cb);
			GuiHelper.addRectToBuffer(buffer, x + 1, y, w - 2, 1, cd);
			GuiHelper.addRectToBuffer(buffer, x + 1, y + h - 1, w - 2, 1, cb);
			GuiHelper.addRectToBuffer(buffer, x + 1, y + 1, w - 2, h - 2, c);

			tessellator.draw();
			GlStateManager.enableTexture2D();
			GlStateManager.color(1F, 1F, 1F, 1F);
		}
	}

	@Override
	public Color4I getContentColor(boolean dark)
	{
		return dark ? COLOR : Color4I.WHITE;
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
	public Icon getSlot(boolean mouseOver)
	{
		return mouseOver ? SLOT_MOUSE_OVER : SLOT;
	}

	@Override
	public Icon getScrollBar(boolean grabbed, boolean vertical)
	{
		return grabbed ? SCROLL_BAR_GRABBED : SCROLL_BAR;
	}

	@Override
	public Icon getCheckbox(boolean mouseOver, boolean selected, boolean radioButton)
	{
		return selected ? (mouseOver ? CHECKBOX_MOUSE_OVER : CHECKBOX) : Icon.EMPTY;
	}
}