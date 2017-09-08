package com.feed_the_beast.ftbl.lib.gui;

import net.minecraft.util.EnumFacing;

/**
 * @author LatvianModder
 */
public class PanelScrollBar extends Slider
{
	public final Panel panel;
	private int elementSize = 1;
	private double scrollStep;
	private boolean autoSize;

	public PanelScrollBar(int x, int y, int w, int h, int ss, Panel p)
	{
		super(x, y, w, h, ss);
		panel = p;
		autoSize = ss <= 0;
	}

	public void setElementSize(int s)
	{
		elementSize = Math.max(1, s);

		if (panel.widgets.isEmpty())
		{
			setScrollStep(0);
		}
		else
		{
			setSrollStepFromOneElementSize(elementSize / panel.widgets.size());
		}

		if (autoSize)
		{
			sliderSize = (int) (height * (double) panel.height / (double) elementSize);
		}
	}

	public void setScrollStep(double v)
	{
		scrollStep = v;
	}

	public void setSrollStepFromOneElementSize(int s)
	{
		setScrollStep(s / (double) (elementSize - (getPlane() == EnumFacing.Plane.VERTICAL ? panel.height : panel.width)));
	}

	@Override
	public boolean canMouseScroll(GuiBase gui)
	{
		return super.canMouseScroll(gui) || gui.isMouseOver(panel);
	}

	@Override
	public double getScrollStep()
	{
		return scrollStep;
	}

	@Override
	public void onMoved(GuiBase gui)
	{
		if (getPlane() == EnumFacing.Plane.VERTICAL)
		{
			panel.setScrollY(getValue(gui), elementSize);
		}
		else
		{
			panel.setScrollX(getValue(gui), elementSize);
		}
	}

	@Override
	public boolean isEnabled(GuiBase gui)
	{
		return elementSize > (getPlane() == EnumFacing.Plane.VERTICAL ? panel.height : panel.width);
	}

	@Override
	public boolean shouldRender(GuiBase gui)
	{
		return isEnabled(gui);
	}
}