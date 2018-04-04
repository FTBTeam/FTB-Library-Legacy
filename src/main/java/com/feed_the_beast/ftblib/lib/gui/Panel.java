package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.FTBLib;
import com.feed_the_beast.ftblib.lib.icon.Color4I;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends Widget
{
	public static final int ONLY_RENDER_WIDGETS_INSIDE = 1;
	public static final int ONLY_INTERACT_WITH_WIDGETS_INSIDE = 2;
	public static final int DEFAULTS = ONLY_RENDER_WIDGETS_INSIDE | ONLY_INTERACT_WITH_WIDGETS_INSIDE;

	public final List<Widget> widgets;
	private int scrollX = 0, scrollY = 0;
	private int offsetX = 0, offsetY = 0;
	private int flags = 0;

	public Panel(Panel panel)
	{
		super(panel);
		widgets = new ArrayList<>();
	}

	public void addFlags(int f)
	{
		flags |= f;
	}

	public boolean hasFlag(int flag)
	{
		return (flags & flag) != 0;
	}

	public abstract void addWidgets();

	public abstract void alignWidgets();

	public void refreshWidgets()
	{
		widgets.clear();
		boolean unicode = hasFlag(UNICODE);

		if (unicode)
		{
			pushFontUnicode(true);
		}

		try
		{
			addWidgets();
		}
		catch (MismatchingParentPanelException ex)
		{
			FTBLib.LOGGER.error(ex.getMessage());
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}

		alignWidgets();

		for (Widget widget : widgets)
		{
			if (widget instanceof Panel)
			{
				((Panel) widget).refreshWidgets();
			}
		}

		alignWidgets();

		if (unicode)
		{
			popFontUnicode();
		}
	}

	public void add(Widget widget)
	{
		if (widget.parent != this)
		{
			throw new MismatchingParentPanelException(this, widget);
		}

		widgets.add(widget);
	}

	public void addAll(Iterable<? extends Widget> list)
	{
		for (Widget w : list)
		{
			add(w);
		}
	}

	protected final int align(WidgetLayout layout)
	{
		return layout.align(this);
	}

	@Override
	public int getAX()
	{
		return super.getAX() + offsetX;
	}

	@Override
	public int getAY()
	{
		return super.getAY() + offsetY;
	}

	public void setOffset(boolean flag)
	{
		if (flag)
		{
			offsetX = -scrollX;
			offsetY = -scrollY;
		}
		else
		{
			offsetX = offsetY = 0;
		}
	}

	public boolean isOffset()
	{
		return offsetX != 0 || offsetY != 0;
	}

	public void setScrollX(int scroll)
	{
		scrollX = scroll;
	}

	public void setScrollY(int scroll)
	{
		scrollY = scroll;
	}

	public int getScrollX()
	{
		return scrollX;
	}

	public int getScrollY()
	{
		return scrollY;
	}

	@Override
	public void draw()
	{
		boolean renderInside = hasFlag(ONLY_RENDER_WIDGETS_INSIDE);
		pushFontUnicode(hasFlag(UNICODE));

		int ax = getAX();
		int ay = getAY();

		drawPanelBackground(ax, ay);

		if (renderInside)
		{
			GuiHelper.pushScissor(getScreen(), ax, ay, width, height);
		}

		setOffset(true);
		drawOffsetPanelBackground(ax + offsetX, ay + offsetY);

		for (int i = 0; i < widgets.size(); i++)
		{
			Widget widget = widgets.get(i);

			if (widget.shouldDraw() && (!renderInside || widget.collidesWith(ax, ay, width, height)))
			{
				drawWidget(widget, i, ax + offsetX, ay + offsetY, width, height);
			}
		}

		setOffset(false);

		if (renderInside)
		{
			GuiHelper.popScissor(getScreen());
		}

		popFontUnicode();
	}

	protected void drawPanelBackground(int ax, int ay)
	{
		getIcon().draw(ax, ay, width, height);
	}

	protected void drawOffsetPanelBackground(int ax, int ay)
	{
	}

	protected void drawWidget(Widget widget, int index, int ax, int ay, int w, int h)
	{
		widget.draw();

		if (GuiBase.renderDebugBoxes)
		{
			Color4I col = Color4I.rgb(java.awt.Color.HSBtoRGB((widget.hashCode() & 255) / 255F, 1F, 1F));
			GuiHelper.drawHollowRect(widget.getAX(), widget.getAY(), widget.width, widget.height, col.withAlpha(150), false);
			col.withAlpha(30).draw(widget.getAX() + 1, widget.getAY() + 1, widget.width - 2, widget.height - 2);
		}
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
		if (hasFlag(ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !isMouseOver())
		{
			return;
		}

		pushFontUnicode(hasFlag(UNICODE));
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget w = widgets.get(i);

			if (w.shouldAddMouseOverText())
			{
				w.addMouseOverText(list);

				if (GuiBase.renderDebugBoxes)
				{
					list.add(TextFormatting.DARK_GRAY + w.getClass().getSimpleName() + ": " + w.width + "x" + w.height);
				}
			}
		}

		setOffset(false);
		popFontUnicode();
	}

	@Override
	public boolean mousePressed(MouseButton button)
	{
		if (hasFlag(ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !isMouseOver())
		{
			return false;
		}

		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget w = widgets.get(i);

			if (w.isEnabled())
			{
				if (w.mousePressed(button))
				{
					setOffset(false);
					return true;
				}
			}
		}

		setOffset(false);
		return false;
	}

	@Override
	public void mouseReleased(MouseButton button)
	{
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget w = widgets.get(i);

			if (w.isEnabled())
			{
				w.mouseReleased(button);
			}
		}

		setOffset(false);
	}

	@Override
	public boolean keyPressed(int key, char keyChar)
	{
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget w = widgets.get(i);

			if (w.isEnabled() && w.keyPressed(key, keyChar))
			{
				setOffset(false);
				return true;
			}
		}

		setOffset(false);
		return false;
	}

	@Override
	public void keyReleased(int key)
	{
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget w = widgets.get(i);

			if (w.isEnabled())
			{
				w.keyReleased(key);
			}
		}

		setOffset(false);
	}
}