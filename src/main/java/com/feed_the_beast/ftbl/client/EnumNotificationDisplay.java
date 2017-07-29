package com.feed_the_beast.ftbl.client;

import com.feed_the_beast.ftbl.lib.NameMap;
import net.minecraft.util.IStringSerializable;

/**
 * @author LatvianModder
 */
public enum EnumNotificationDisplay implements IStringSerializable
{
	OFF("off"),
	SCREEN("screen"),
	CHAT("chat"),
	CHAT_ALL("chat_all");

	private final String name;

	EnumNotificationDisplay(String n)
	{
		name = n;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public static final NameMap<EnumNotificationDisplay> NAME_MAP = NameMap.create(SCREEN, values());
}