package com.feed_the_beast.ftblib.lib;

import com.feed_the_beast.ftblib.lib.util.misc.NameMap;

/**
 * @author LatvianModder
 */
public enum EnumMessageLocation
{
	OFF("options.narrator.off"),
	CHAT("options.chat.visibility"),
	ACTION_BAR("action_bar");

	public static final NameMap<EnumMessageLocation> NAME_MAP = NameMap.create(CHAT, NameMap.ObjectProperties.withTranslatedName(o -> o.langKey), values());

	public final String langKey;

	EnumMessageLocation(String k)
	{
		langKey = k;
	}
}