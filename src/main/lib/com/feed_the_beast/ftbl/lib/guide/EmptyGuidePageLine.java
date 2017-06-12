package com.feed_the_beast.ftbl.lib.guide;

import com.feed_the_beast.ftbl.api.guide.IGuideTextLine;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;

/**
 * @author LatvianModder
 */
public abstract class EmptyGuidePageLine implements IGuideTextLine
{
	@Override
	public String getUnformattedText()
	{
		return "";
	}

	@Override
	public JsonElement getJson()
	{
		return JsonNull.INSTANCE;
	}
}