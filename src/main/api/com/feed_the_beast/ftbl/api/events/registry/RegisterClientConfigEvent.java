package com.feed_the_beast.ftbl.api.events.registry;

import com.feed_the_beast.ftbl.api.events.FTBLibEvent;
import com.feed_the_beast.ftbl.api.gui.IDrawableObject;
import com.feed_the_beast.ftbl.lib.TriFunction;

/**
 * @author LatvianModder
 */
public class RegisterClientConfigEvent extends FTBLibEvent
{
	private TriFunction<Object, String, String, IDrawableObject> callback;

	public RegisterClientConfigEvent(TriFunction<Object, String, String, IDrawableObject> c)
	{
		callback = c;
	}

	public void register(String id, String title, IDrawableObject icon)
	{
		callback.apply(id, title, icon);
	}
}