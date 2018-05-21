package com.feed_the_beast.ftblib.lib;

import com.feed_the_beast.ftblib.lib.util.misc.NameMap;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * @author LatvianModder
 */
public enum EnumMessageLocation
{
	OFF("options.narrator.off"),
	CHAT("options.chat.visibility"),
	ACTION_BAR("action_bar");

	public static final NameMap<EnumMessageLocation> NAME_MAP = NameMap.create(CHAT, NameMap.ObjectProperties.withName((sender, value) -> new TextComponentTranslation(value.langKey)), values());

	public final String langKey;

	EnumMessageLocation(String k)
	{
		langKey = k;
	}
}