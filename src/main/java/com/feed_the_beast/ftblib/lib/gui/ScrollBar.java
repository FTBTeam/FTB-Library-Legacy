package com.feed_the_beast.ftblib.lib.gui;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.math.MathUtils;
import com.feed_the_beast.ftblib.lib.util.misc.MouseButton;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class ScrollBar extends Widget
{
	public enum Plane
	{
		HORIZONTAL,
		VERTICAL;

		public boolean isVertical()
		{
			return this == VERTICAL;
		}
	}

	public int sliderSize;
	private double value;
	private int grab = -10000;

	public ScrollBar(GuiBase gui, int x, int y, int w, int h, int ss)
	{
		super(gui, x, y, w, h);
		sliderSize = Math.max(ss, 1);
	}

	@Override
	public boolean mousePressed(MouseButton button)
	{
		if (gui.isMouseOver(this))
		{
			grab = (getPlane().isVertical() ? (gui.getMouseY() - (getAY() + getValueI(height))) : (gui.getMouseX() - (getAX() + getValueI(width))));
			return true;
		}

		return false;
	}

	@Override
	public void addMouseOverText(List<String> list)
	{
		double min = getDisplayMin();
		double max = getDisplayMax();

		if (min < max)
		{
			String s = "" + (int) MathUtils.map(value, 0D, 1D, min, max);
			String t = getTitle();
			list.add(t.isEmpty() ? s : (t + ": " + s));
		}
	}

	@Override
	public void renderWidget()
	{
		int ax = getAX();
		int ay = getAY();

		if (isEnabled())
		{
			double v = getValue();
			double v0 = v;

			if (grab != -10000)
			{
				if (gui.isMouseButtonDown(0))
				{
					if (getPlane().isVertical())
					{
						v = (gui.getMouseY() - (ay + grab)) / (double) (height - sliderSize);
					}
					else
					{
						v = (gui.getMouseX() - (ax + grab)) / (double) (width - sliderSize);
					}
				}
				else
				{
					grab = -10000;
				}
			}

			if (gui.getMouseWheel() != 0 && gui.isShiftDown() != getPlane().isVertical() && canMouseScroll())
			{
				v += (gui.getMouseWheel() < 0) ? getScrollStep() : -getScrollStep();
			}

			v = MathHelper.clamp(v, 0D, 1D);

			if (v0 != v)
			{
				setValue(v);
			}
		}

		getBackground().draw(ax, ay, width, height);

		if (getPlane().isVertical())
		{
			if (sliderSize < height)
			{
				getIcon().draw(ax, ay + getValueI(height), width, sliderSize);
			}
		}
		else if (sliderSize < width)
		{
			getIcon().draw(ax + getValueI(width), ay, sliderSize, height);
		}
	}

	public Icon getBackground()
	{
		return gui.getTheme().getScrollBarBackground();
	}

	@Override
	public Icon getIcon()
	{
		return gui.getTheme().getScrollBar(grab != -10000, getPlane().isVertical());
	}

	public void onMoved()
	{
	}

	public boolean canMouseScroll()
	{
		return gui.isMouseOver(this);
	}

	public void setValue(double v)
	{
		if (value != v)
		{
			value = MathHelper.clamp(v, 0D, 1D);
			onMoved();
		}
	}

	public double getValue()
	{
		return value;
	}

	public int getValueI(int max)
	{
		return (int) (getValue() * (max - sliderSize));
	}

	public double getScrollStep()
	{
		return 0.1D;
	}

	public Plane getPlane()
	{
		return Plane.VERTICAL;
	}

	public double getDisplayMin()
	{
		return 0;
	}

	public double getDisplayMax()
	{
		return 0;
	}
}