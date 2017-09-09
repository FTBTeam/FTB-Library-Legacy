package com.feed_the_beast.ftbl.lib.gui;

import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.MouseButton;
import com.feed_the_beast.ftbl.lib.icon.Icon;
import com.feed_the_beast.ftbl.lib.icon.TexturelessRectangle;
import com.feed_the_beast.ftbl.lib.math.MathUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class Slider extends Widget
{
	public static final Icon DEFAULT_SLIDER = new TexturelessRectangle(Color4I.WHITE_A[33]).setLineColor(Button.DEFAULT_BACKGROUND.getLineColor());
	public static final Icon DEFAULT_BACKGROUND = Button.DEFAULT_BACKGROUND;

	public int sliderSize;
	private double value;
	private int grab = -10000;
	public Icon slider = DEFAULT_SLIDER, background = DEFAULT_BACKGROUND;

	public Slider(int x, int y, int w, int h, int ss)
	{
		super(x, y, w, h);
		sliderSize = Math.max(ss, 1);
	}

	@Override
	public boolean mousePressed(GuiBase gui, MouseButton button)
	{
		if (gui.isMouseOver(this))
		{
			grab = (getPlane() == EnumFacing.Plane.VERTICAL ? (gui.getMouseY() - (getAY() + getValueI(gui, height))) : (gui.getMouseX() - (getAX() + getValueI(gui, width))));
			return true;
		}

		return false;
	}

	@Override
	public void addMouseOverText(GuiBase gui, List<String> list)
	{
		double min = getDisplayMin();
		double max = getDisplayMax();

		if (min < max)
		{
			String s = "" + (int) MathUtils.map(value, 0D, 1D, min, max);
			String t = getTitle(gui);
			list.add(t.isEmpty() ? s : (t + ": " + s));
		}
	}

	@Override
	public void renderWidget(GuiBase gui)
	{
		int ax = getAX();
		int ay = getAY();

		if (isEnabled(gui))
		{
			double v = getValue(gui);
			double v0 = v;

			if (grab != -10000)
			{
				if (gui.isMouseButtonDown(0))
				{
					if (getPlane() == EnumFacing.Plane.VERTICAL)
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

			if (gui.getMouseWheel() != 0 && canMouseScroll(gui) && gui.isShiftDown() == (getPlane() == EnumFacing.Plane.HORIZONTAL))
			{
				v += (gui.getMouseWheel() < 0) ? getScrollStep() : -getScrollStep();
			}

			v = MathHelper.clamp(v, 0D, 1D);

			if (v0 != v)
			{
				setValue(gui, v);
			}
		}

		background.draw(ax, ay, width, height, Color4I.NONE);

		if (getPlane() == EnumFacing.Plane.VERTICAL)
		{
			if (sliderSize < height)
			{
				slider.draw(ax, ay + getValueI(gui, height), width, sliderSize, Color4I.NONE);
			}
		}
		else if (sliderSize < width)
		{
			slider.draw(ax + getValueI(gui, width), ay, sliderSize, height, Color4I.NONE);
		}
	}

	public void onMoved(GuiBase gui)
	{
	}

	public boolean canMouseScroll(GuiBase gui)
	{
		return gui.isMouseOver(this);
	}

	public void setValue(GuiBase gui, double v)
	{
		if (value != v)
		{
			value = MathHelper.clamp(v, 0D, 1D);
			onMoved(gui);
		}
	}

	public double getValue(GuiBase gui)
	{
		return value;
	}

	public int getValueI(GuiBase gui, int max)
	{
		return (int) (getValue(gui) * (max - sliderSize));
	}

	public double getScrollStep()
	{
		return 0.1D;
	}

	public EnumFacing.Plane getPlane()
	{
		return EnumFacing.Plane.VERTICAL;
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