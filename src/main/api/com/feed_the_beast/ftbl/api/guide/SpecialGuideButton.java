package com.feed_the_beast.ftbl.api.guide;

import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.client.ImageProvider;
import com.feed_the_beast.ftbl.lib.util.JsonUtils;
import com.feed_the_beast.ftbl.lib.util.StringUtils;
import com.google.gson.JsonObject;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.event.ClickEvent;

/**
 * @author LatvianModder
 */
public class SpecialGuideButton
{
	public final ITextComponent title;
	public final IDrawableObject icon;
	public final ClickEvent clickEvent;

	public SpecialGuideButton(ITextComponent t, IDrawableObject icn, ClickEvent c)
	{
		title = t;
		icon = icn;
		clickEvent = c;
	}

	public SpecialGuideButton(JsonObject o)
	{
		title = o.has("title") ? JsonUtils.deserializeTextComponent(o.get("title")) : StringUtils.text("");
		icon = o.has("icon") ? ImageProvider.get(o.get("icon")) : ImageProvider.NULL;
		clickEvent = o.has("click") ? JsonUtils.deserializeClickEvent(o.get("click")) : null;
	}

	public JsonObject serialize()
	{
		JsonObject o = new JsonObject();
		o.add("title", JsonUtils.serializeTextComponent(title));
		o.add("icon", icon.getJson());
		if (clickEvent != null)
		{
			o.add("click", JsonUtils.serializeClickEvent(clickEvent));
		}
		return o;
	}
}