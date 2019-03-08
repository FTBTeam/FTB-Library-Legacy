package com.feed_the_beast.ftblib.integration;

import com.feed_the_beast.ftblib.lib.gui.GuiContainerWrapper;
import com.feed_the_beast.ftblib.lib.gui.Panel;
import com.feed_the_beast.ftblib.lib.gui.Widget;
import mezz.jei.api.gui.IGhostIngredientHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author LatvianModder
 */
public class JEIGhostItemHandler implements IGhostIngredientHandler<GuiContainerWrapper>
{
	@Override
	public <I> List<Target<I>> getTargets(GuiContainerWrapper gui, I ingredient, boolean doStart)
	{
		List<Target<I>> list = new ArrayList<>();
		getTargets(list, ingredient, gui.getGui());
		Collections.reverse(list);
		return list;
	}

	private <I> void getTargets(List<Target<I>> list, Object ingredient, Panel panel)
	{
		for (Widget widget : panel.widgets)
		{
			if (widget.isJEIGhostTarget(ingredient))
			{
				list.add((Target<I>) new WidgetTarget(widget));
			}

			if (widget instanceof Panel)
			{
				getTargets(list, ingredient, (Panel) widget);
			}
		}
	}

	@Override
	public void onComplete()
	{
	}
}