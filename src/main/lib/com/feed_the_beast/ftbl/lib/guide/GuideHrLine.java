package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.feed_the_beast.ftbl.lib.Color4I;
import com.feed_the_beast.ftbl.lib.gui.GuiBase;
import com.feed_the_beast.ftbl.lib.gui.GuiHelper;
import com.feed_the_beast.ftbl.lib.gui.Panel;
import com.feed_the_beast.ftbl.lib.gui.Widget;
import com.feed_the_beast.ftbl.lib.util.ColorUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

/**
 * @author LatvianModder
 */
public class GuideHrLine extends EmptyGuidePageLine
{
	public final int height;
	public final Color4I color;

	public GuideHrLine(int h, Color4I c)
	{
		height = h;
		color = c;
	}

	public GuideHrLine(JsonElement e)
	{
		JsonObject o = e.getAsJsonObject();
		height = o.has("height") ? Math.max(1, o.get("height").getAsInt()) : 1;
		color = o.has("color") ? new Color4I(true, 0xFF000000 | ColorUtils.deserialize(o.get("color"))) : Color4I.NONE;
	}

	@Override
	public Widget createWidget(GuiBase gui, Panel parent)
	{
		return new WidgetGuideHr(parent);
	}

	@Override
	public IGuideTextLine copy(GuidePage page)
	{
		return new GuideHrLine(height, color);
	}

	@Override
	public JsonElement getJson()
	{
		JsonObject o = new JsonObject();
		o.add("id", new JsonPrimitive("hr"));
		o.add("height", new JsonPrimitive(height));
		o.add("color", color.toJson());
		return o;
	}

	private class WidgetGuideHr extends Widget
	{
		private WidgetGuideHr(Panel parent)
		{
			super(0, 1, parent.width, GuideHrLine.this.height + 2);
		}

		@Override
		public void renderWidget(GuiBase gui)
		{
			GuiHelper.drawBlankRect(getAX(), getAY() + 1, width, GuideHrLine.this.height, color.hasColor() ? color : gui.getContentColor());
		}
	}

	@Override
	public boolean isEmpty()
	{
		return false;
	}
}