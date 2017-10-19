package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.util.misc.MouseButton;

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

	public Panel(GuiBase gui, int x, int y, int w, int h)
	{
		super(gui, x, y, w, h);
		widgets = new ArrayList<>();
	}

	public Panel(GuiBase gui)
	{
		this(gui, 0, 0, 0, 0);
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

	public void refreshWidgets()
	{
		widgets.clear();
		addWidgets();
		updateWidgetPositions();
	}

	public void add(Widget widget)
	{
		widget.setParentPanel(this);
		widgets.add(widget);

		if (widget instanceof Panel)
		{
			((Panel) widget).refreshWidgets();
		}
	}

	public void addAll(Widget... widgets)
	{
		for (Widget w : widgets)
		{
			add(w);
		}
	}

	public void addAll(Iterable<? extends Widget> list)
	{
		for (Widget w : list)
		{
			add(w);
		}
	}

	public void updateWidgetPositions()
	{
	}

	public void setScrollX(double scroll, int elementsWidth)
	{
		if (elementsWidth < width)
		{
			setScrollX(0);
		}
		else
		{
			setScrollX((int) (scroll * (elementsWidth - width)));
		}
	}

	public void setScrollY(double scroll, int elementsHeight)
	{
		if (elementsHeight < height)
		{
			setScrollY(0);
		}
		else
		{
			setScrollY((int) (scroll * (elementsHeight - height)));
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

	@Override
	public void renderWidget()
	{
		boolean renderInside = hasFlag(ONLY_RENDER_WIDGETS_INSIDE);
		gui.pushFontUnicode(hasFlag(UNICODE));

		int ax = getAX();
		int ay = getAY();

		renderPanelBackground(ax, ay);

		if (renderInside)
		{
			GuiHelper.pushScissor(gui.getScreen(), ax, ay, width, height);
		}

		setOffset(true);

		for (int i = 0; i < widgets.size(); i++)
		{
			Widget widget = widgets.get(i);

			if (widget.shouldRender() && (!renderInside || widget.collidesWith(ax, ay, width, height)))
			{
				renderWidget(widget, i, ax + offsetX, ay + offsetY, width, height);
			}
		}

		setOffset(false);

		if (renderInside)
		{
			GuiHelper.popScissor(gui.getScreen());
		}

		gui.popFontUnicode();
	}

	protected void renderPanelBackground(int ax, int ay)
	{
		getIcon().draw(ax, ay, width, height);
	}

	protected void renderWidget(Widget widget, int index, int ax, int ay, int w, int h)
	{
		widget.renderWidget();
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
		if (hasFlag(ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !gui.isMouseOver(this))
		{
			return;
		}

		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget w = widgets.get(i);

			if (w.shouldAddMouseOverText())
			{
				w.addMouseOverText(list);
			}
		}

		setOffset(false);
	}

	@Override
	public boolean mousePressed(MouseButton button)
	{
		if (hasFlag(ONLY_INTERACT_WITH_WIDGETS_INSIDE) && !gui.isMouseOver(this))
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
	public void mouseReleased()
	{
		setOffset(true);

		for (int i = widgets.size() - 1; i >= 0; i--)
		{
			Widget w = widgets.get(i);

			if (w.isEnabled())
			{
				w.mouseReleased();
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
}