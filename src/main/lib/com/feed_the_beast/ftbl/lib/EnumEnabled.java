package com.feed_the_beast.ftbl.lib;

/**
 * @author LatvianModder
 */
public enum EnumEnabled
{
	ENABLED,
	DISABLED;

	public static final NameMap<EnumEnabled> NAME_MAP = NameMap.create(DISABLED, values());
}