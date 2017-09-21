package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.gui.WidgetLayout;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LatvianModder
 */
public class GuideContentsLine extends EmptyGuidePageLine
{
	private final GuidePage page;

	public GuideContentsLine(GuidePage p)
	{
		page = p;
	}

	@Override
	public Widget createWidget(GuiBase gui, Panel parent)
	{
		return new PanelGuideContents(gui);
	}

	@Override
	public IGuideTextLine copy(GuidePage page)
	{
		return new GuideContentsLine(page);
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		o.addProperty("id", "contents");
		return o;
	}

	private class PanelGuideContents extends Panel
	{
		private final List<Widget> buttons;

		private PanelGuideContents(GuiBase gui)
		{
			super(0, 0, 10, 10);
			buttons = new ArrayList<>();
			addButtons(gui, page, 0);
		}

		private void addButtons(GuiBase gui, GuidePage from, int level)
		{
			setWidth(10000);

			for (GuidePage p : from.childPages.values())
			{
				Widget w = new ButtonGuidePage(gui, p, true);
				w.posX = level * 12;
				w.width = 1000;
				buttons.add(w);
				addButtons(gui, p, level + 1);
			}
		}

		@Override
		public void addWidgets()
		{
			addAll(buttons);
			updateWidgetPositions();
		}

		@Override
		public void updateWidgetPositions()
		{
			setHeight(align(WidgetLayout.VERTICAL));
		}
	}

	@Override
	public boolean isEmpty()
	{
		return page.childPages.isEmpty();
	}
}