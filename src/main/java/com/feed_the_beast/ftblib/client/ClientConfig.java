package com.feed_the_beast.ftblib.client;

import com.feed_the_beast.ftblib.lib.icon.Icon;
import com.feed_the_beast.ftblib.lib.util.JsonUtils;
import com.google.gson.JsonObject;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;

/**
 * @author LatvianModder
 */
public class ClientConfig
{
	public final String id;
	public final ITextComponent name;
	public final Icon icon;

	public ClientConfig(String _id, @Nullable ITextComponent _name, Icon _icon)
	{
		id = _id;
		name = _name == null ? new TextComponentString(id) : _name;
		icon = _icon;
	}

	public ClientConfig(JsonObject o)
	{
		this(o.get("id").getAsString(), JsonUtils.deserializeTextComponent(o.get("name")), Icon.getIcon(o.get("icon")));
	}
}